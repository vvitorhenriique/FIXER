package com.fixer.servicosapp

import android.content.Intent
import com.fixer.servicosapp.data.*
import org.junit.Assert.*
import org.junit.Test

class ContatoPrestadorTest {
    @Test fun contatosUsamNumeroNormalizadoEAbremDiscadorSemLigarAutomaticamente() {
        val prestador = Prestador("teste", "Teste", "Pintor", "Pintura", "+55 (11) 98765-4321")
        val whats = intentDeContato(prestador, TipoContato.WHATSAPP)!!
        assertEquals(Intent.ACTION_VIEW, whats.action)
        assertEquals("https://wa.me/5511987654321", whats.data.toString())
        val ligacao = intentDeContato(prestador, TipoContato.LIGACAO)!!
        assertEquals(Intent.ACTION_DIAL, ligacao.action)
        assertEquals("tel:+5511987654321", ligacao.data.toString())
        assertNull(intentDeContato(prestador.copy(telefone = ""), TipoContato.WHATSAPP))
    }
}
