package com.fixer.servicosapp

import com.fixer.servicosapp.data.*
import org.junit.Assert.*
import org.junit.Test

class PrestadorTest {
    private val lista = listOf(
        Prestador("a", "João da Silva", "Eletricista", "Instalações"),
        Prestador("b", "Ana Martins", "Encanador", "Reparos"),
        Prestador("c", "Ana Souza", "Eletricista", "Manutenção")
    )

    @Test fun pesquisaIgnoraAcentosCaixaEEspacosECombinaNomeComCategoria() {
        assertEquals(listOf(lista[0]), filtrarPrestadores(lista, "  JOAO  eletricista ", "Todos"))
        assertEquals(listOf(lista[1], lista[2]), filtrarPrestadores(lista, "ana", "Todos"))
        assertEquals(listOf(lista[2]), filtrarPrestadores(lista, "ANA", "Eletricista"))
        assertTrue(filtrarPrestadores(lista, "joao", "Encanador").isEmpty())
        assertEquals(lista, filtrarPrestadores(lista, " ", "Todos"))
    }

    @Test fun telefoneAceitaDddECodigoBrasilSemDuplicarOPrefixo() {
        assertEquals("11987654321", normalizarTelefone("+55 (11) 98765-4321"))
        assertEquals("1133334444", normalizarTelefone("(11) 3333-4444"))
        assertEquals("55987654321", normalizarTelefone("(55) 98765-4321"))
        assertEquals("(11) 98765-4321", formatarTelefone("5511987654321"))
        listOf("", "9999", "00123456789", "+1 212 555 1234", "abc11987654321").forEach {
            assertNull("Deveria rejeitar $it", normalizarTelefone(it))
        }
    }
}
