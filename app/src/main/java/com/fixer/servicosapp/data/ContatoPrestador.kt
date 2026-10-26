package com.fixer.servicosapp.data

import android.content.Intent
import android.net.Uri

enum class TipoContato { WHATSAPP, LIGACAO }

fun intentDeContato(prestador: Prestador, tipo: TipoContato): Intent? {
    val numero = normalizarTelefone(prestador.telefone) ?: return null
    return when (tipo) {
        TipoContato.WHATSAPP -> Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/55$numero"))
        TipoContato.LIGACAO -> Intent(Intent.ACTION_DIAL, Uri.parse("tel:+55$numero"))
    }
}
