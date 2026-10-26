package com.fixer.servicosapp.data

import java.text.Normalizer
import java.util.Locale

data class Prestador(
    val id: String,
    val nome: String,
    val categoria: String,
    val descricao: String,
    val telefone: String = "",
    val avaliacao: String? = null,
    val exemplo: Boolean = false
)

fun normalizarTelefone(valor: String): String? {
    if (valor.any { !it.isDigit() && it !in " +()- ." }) return null
    val digitos = valor.filter { it in '0'..'9' }
    if (valor.trim().startsWith("+") && !digitos.startsWith("55")) return null
    val nacional = if (digitos.length in 12..13 && digitos.startsWith("55")) digitos.drop(2) else digitos
    return nacional.takeIf { it.length in 10..11 && (it.take(2).toIntOrNull() ?: 0) in 11..99 }
}

fun formatarTelefone(valor: String): String {
    val numero = normalizarTelefone(valor) ?: return "Não informado"
    return "(${numero.take(2)}) ${numero.substring(2, numero.length - 4)}-${numero.takeLast(4)}"
}

fun textoDeBusca(valor: String): String = Normalizer.normalize(valor, Normalizer.Form.NFD)
    .replace("\\p{M}+".toRegex(), "").lowercase(Locale.ROOT).trim()

fun filtrarPrestadores(lista: List<Prestador>, busca: String, categoria: String): List<Prestador> {
    val termos = textoDeBusca(busca).split("\\s+".toRegex()).filter { it.isNotBlank() }
    return lista.filter { prestador ->
        val texto = textoDeBusca("${prestador.nome} ${prestador.categoria}")
        (categoria == "Todos" || prestador.categoria == categoria) && termos.all { it in texto }
    }
}

// Perfis fictícios para explorar o aplicativo sem publicar contatos de terceiros.
val prestadoresExemplo = listOf(
    Prestador("exemplo:ana", "Ana Martins", "Eletricista", "Instalações elétricas, troca de tomadas e iluminação. Atendimento cuidadoso para deixar sua casa funcionando bem.", avaliacao = "4,9", exemplo = true),
    Prestador("exemplo:rafael", "Rafael Costa", "Encanador", "Reparo de vazamentos, instalação de torneiras e manutenção hidráulica residencial. Explico o serviço antes de começar.", avaliacao = "4,8", exemplo = true),
    Prestador("exemplo:camila", "Camila Oliveira", "Diarista", "Limpeza e organização de ambientes residenciais, com atenção aos detalhes e aos cuidados de cada espaço.", avaliacao = "4,9", exemplo = true),
    Prestador("exemplo:bruno", "Bruno Almeida", "Pintor", "Pintura de paredes, preparação de superfícies e pequenos retoques. Acabamento organizado para renovar seu ambiente.", avaliacao = "4,7", exemplo = true),
    Prestador("exemplo:marcos", "Marcos Souza", "Pedreiro", "Pequenas reformas, assentamento de pisos e reparos em alvenaria. Planejamento e cuidado em cada etapa do trabalho.", avaliacao = "4,8", exemplo = true)
)
