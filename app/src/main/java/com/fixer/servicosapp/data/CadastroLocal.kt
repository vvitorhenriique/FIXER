package com.fixer.servicosapp.data

import android.content.Context
import android.util.Patterns
import org.json.JSONObject
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64
import java.util.Locale
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

val categoriasServico = listOf("Eletricista", "Encanador", "Pedreiro", "Pintor", "Diarista", "Outros")

data class PerfilLocal(
    val nome: String,
    val email: String,
    val tipo: String,
    val categoria: String = "",
    val descricao: String = ""
)

fun validarCadastro(
    nome: String, email: String, senha: String,
    tipo: String, categoria: String, descricao: String
): String? = when {
    nome.trim().length < 2 -> "Informe seu nome completo."
    !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> "Informe um e-mail válido."
    senha.length < 6 -> "A senha precisa ter pelo menos 6 caracteres."
    tipo !in listOf("Cliente", "Prestador") -> "Escolha Cliente ou Prestador."
    tipo == "Prestador" && categoria !in categoriasServico -> "Escolha a categoria do seu serviço."
    tipo == "Prestador" && descricao.isBlank() -> "Descreva brevemente o serviço que você oferece."
    else -> null
}

/** Contas de demonstração privadas deste aplicativo, salvas somente neste aparelho. */
class CadastroLocal(context: Context, nomeArquivo: String = "cadastros_locais") {
    private val preferencias = context.applicationContext.getSharedPreferences(nomeArquivo, Context.MODE_PRIVATE)

    fun cadastrar(
        nome: String, email: String, senha: String,
        tipo: String, categoria: String, descricao: String
    ): Result<PerfilLocal> = runCatching {
        validarCadastro(nome, email, senha, tipo, categoria, descricao)?.let {
            throw IllegalArgumentException(it)
        }
        val emailNormalizado = email.trim().lowercase(Locale.ROOT)
        val chave = "conta:$emailNormalizado"
        synchronized(CadastroLocal::class.java) {
            require(!preferencias.contains(chave)) { "Este e-mail já está cadastrado neste aparelho. Entre com sua senha." }
            val perfil = PerfilLocal(
                nome.trim(), emailNormalizado, tipo,
                if (tipo == "Prestador") categoria else "",
                if (tipo == "Prestador") descricao.trim() else ""
            )
            val salt = ByteArray(16).also { SecureRandom().nextBytes(it) }
            val registro = JSONObject()
                .put("nome", perfil.nome).put("email", perfil.email)
                .put("tipo", perfil.tipo).put("categoria", perfil.categoria)
                .put("descricao", perfil.descricao)
                .put("salt", Base64.getEncoder().encodeToString(salt))
                .put("hash", Base64.getEncoder().encodeToString(derivarSenha(senha, salt)))
            check(preferencias.edit().putString(chave, registro.toString()).commit()) {
                "Não foi possível salvar o cadastro. Tente novamente."
            }
            perfil
        }
    }

    fun entrar(email: String, senha: String): Result<PerfilLocal> = runCatching {
        val emailNormalizado = email.trim().lowercase(Locale.ROOT)
        val dados = preferencias.getString("conta:$emailNormalizado", null)
        require(dados != null) { "E-mail ou senha incorretos." }
        val registro = JSONObject(dados)
        val salt = Base64.getDecoder().decode(registro.getString("salt"))
        val hashEsperado = Base64.getDecoder().decode(registro.getString("hash"))
        require(MessageDigest.isEqual(hashEsperado, derivarSenha(senha, salt))) {
            "E-mail ou senha incorretos."
        }
        PerfilLocal(
            registro.getString("nome"), registro.getString("email"), registro.getString("tipo"),
            registro.optString("categoria"), registro.optString("descricao")
        )
    }

    private fun derivarSenha(senha: String, salt: ByteArray): ByteArray {
        val especificacao = PBEKeySpec(senha.toCharArray(), salt, 120_000, 256)
        return try {
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(especificacao).encoded
        } finally {
            especificacao.clearPassword()
        }
    }
}
