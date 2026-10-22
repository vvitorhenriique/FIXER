package com.fixer.servicosapp

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.fixer.servicosapp.data.CadastroLocal
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import java.util.UUID

class CadastroLocalTest {
    private val contexto = InstrumentationRegistry.getInstrumentation().targetContext
    private val arquivo = "teste_cadastro_${UUID.randomUUID()}"
    private val cadastro = CadastroLocal(contexto, arquivo)

    @After
    fun limparDadosDeTeste() {
        contexto.deleteSharedPreferences(arquivo)
    }

    @Test
    fun contaPersisteSemSalvarSenhaEmTextoEAutentica() {
        val perfil = cadastro.cadastrar("Ana Silva", "  ANA@example.com ", "teste123", "Cliente", "", "").getOrThrow()
        val registro = contexto.getSharedPreferences(arquivo, Context.MODE_PRIVATE).getString("conta:ana@example.com", "")!!
        assertFalse(registro.contains("teste123"))
        assertEquals("ana@example.com", perfil.email)
        assertEquals(perfil, CadastroLocal(contexto, arquivo).entrar("ANA@example.com", "teste123").getOrThrow())
        assertTrue(cadastro.entrar("ana@example.com", "senhaerrada").isFailure)
        assertTrue(cadastro.entrar("inexistente@example.com", "teste123").isFailure)
    }

    @Test
    fun rejeitaDuplicataSemSubstituirConta() {
        cadastro.cadastrar("Ana Silva", "ana@example.com", "teste123", "Cliente", "", "").getOrThrow()
        assertTrue(cadastro.cadastrar("Outra pessoa", "ANA@example.com", "outrasenha", "Cliente", "", "").isFailure)
        assertEquals("Ana Silva", cadastro.entrar("ana@example.com", "teste123").getOrThrow().nome)
    }

    @Test
    fun prestadorExigeCategoriaEDescricaoEClienteNaoGuardaEssesCampos() {
        assertTrue(cadastro.cadastrar("João Silva", "joao@example.com", "teste123", "Prestador", "", "").isFailure)
        assertTrue(cadastro.cadastrar("João Silva", "joao@example.com", "teste123", "Prestador", "Eletricista", " ").isFailure)
        val prestador = cadastro.cadastrar("João Silva", "joao@example.com", "teste123", "Prestador", "Eletricista", "Instalações residenciais").getOrThrow()
        assertEquals("Eletricista", prestador.categoria)
        assertEquals(prestador, cadastro.entrar("joao@example.com", "teste123").getOrThrow())
        val cliente = cadastro.cadastrar("Ana Silva", "ana@example.com", "teste123", "Cliente", "Eletricista", "Anterior").getOrThrow()
        assertEquals("", cliente.categoria)
        assertEquals("", cliente.descricao)
        assertTrue(cadastro.cadastrar("", "erro", "123", "Cliente", "", "").isFailure)
    }
}
