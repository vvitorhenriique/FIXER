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
    val descricao: String = "",
    val telefone: String = ""
)

fun validarCadastro(
    nome: String, email: String, senha: String,
    tipo: String, categoria: String, descricao: String, telefone: String = ""
): String? = when {
    nome.trim().length < 2 -> "Informe seu nome completo."
    !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> "Informe um e-mail válido."
    senha.length < 6 -> "A senha precisa ter pelo menos 6 caracteres."
    tipo !in listOf("Cliente", "Prestador") -> "Escolha Cliente ou Prestador."
    tipo == "Prestador" && categoria !in categoriasServico -> "Escolha a categoria do seu serviço."
    tipo == "Prestador" && descricao.isBlank() -> "Descreva brevemente o serviço que você oferece."
    tipo == "Prestador" && normalizarTelefone(telefone) == null -> "Informe um telefone válido com DDD (10 ou 11 dígitos)."
    else -> null
}

/** Contas de demonstração privadas deste aplicativo, salvas somente neste aparelho. */
class CadastroLocal(context: Context, nomeArquivo: String = "cadastros_locais") {
    private val preferencias = context.applicationContext.getSharedPreferences(nomeArquivo, Context.MODE_PRIVATE)

    fun cadastrar(
        nome: String, email: String, senha: String,
        tipo: String, categoria: String, descricao: String, telefone: String = ""
    ): Result<PerfilLocal> = runCatching {
        validarCadastro(nome, email, senha, tipo, categoria, descricao, telefone)?.let {
            throw IllegalArgumentException(it)
        }
        val emailNormalizado = email.trim().lowercase(Locale.ROOT)
        val chave = "conta:$emailNormalizado"
        synchronized(CadastroLocal::class.java) {
            require(!preferencias.contains(chave)) { "Este e-mail já está cadastrado neste aparelho. Entre com sua senha." }
            val perfil = PerfilLocal(
                nome.trim(), emailNormalizado, tipo,
                if (tipo == "Prestador") categoria else "",
                if (tipo == "Prestador") descricao.trim() else "",
                if (tipo == "Prestador") normalizarTelefone(telefone).orEmpty() else ""
            )
            val salt = ByteArray(16).also { SecureRandom().nextBytes(it) }
            val registro = JSONObject()
                .put("nome", perfil.nome).put("email", perfil.email)
                .put("tipo", perfil.tipo).put("categoria", perfil.categoria)
                .put("descricao", perfil.descricao)
                .put("telefone", perfil.telefone)
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
        lerPerfil(registro)
    }

    fun obterPerfil(email: String): PerfilLocal? = preferencias.getString("conta:$email", null)
        ?.let { runCatching { lerPerfil(JSONObject(it)) }.getOrNull() }

    fun prestadores(): List<Prestador> = preferencias.all.entries
        .filter { it.key.startsWith("conta:") }
        .mapNotNull { (_, valor) ->
            runCatching { lerPerfil(JSONObject(valor as String)) }.getOrNull()
        }
        .filter { it.tipo == "Prestador" }
        .map { Prestador("local:${it.email}", it.nome, it.categoria, it.descricao, it.telefone) }
        .sortedBy { textoDeBusca(it.nome) }

    fun atualizarTelefone(email: String, telefone: String): Result<PerfilLocal> = runCatching {
        val numero = requireNotNull(normalizarTelefone(telefone)) {
            "Informe um telefone válido com DDD (10 ou 11 dígitos)."
        }
        synchronized(CadastroLocal::class.java) {
            val chave = "conta:$email"
            val registro = JSONObject(requireNotNull(preferencias.getString(chave, null)))
            require(registro.getString("tipo") == "Prestador")
            registro.put("telefone", numero)
            check(preferencias.edit().putString(chave, registro.toString()).commit()) {
                "Não foi possível salvar o contato. Tente novamente."
            }
            lerPerfil(registro)
        }
    }

    fun emailDaSessao(): String? = preferencias.getString("sessao_email", null)
        ?.takeIf { obterPerfil(it) != null }

    fun manterSessao(email: String?) {
        preferencias.edit().apply {
            if (email == null) remove("sessao_email") else putString("sessao_email", email)
        }.apply()
    }

    private fun lerPerfil(registro: JSONObject) = PerfilLocal(
        registro.getString("nome"), registro.getString("email"), registro.getString("tipo"),
        registro.optString("categoria"), registro.optString("descricao"), registro.optString("telefone")
    )

    private fun derivarSenha(senha: String, salt: ByteArray): ByteArray {
        val especificacao = PBEKeySpec(senha.toCharArray(), salt, 120_000, 256)
        return try {
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(especificacao).encoded
        } finally {
            especificacao.clearPassword()
        }
    }
}
