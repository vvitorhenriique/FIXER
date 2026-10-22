package com.fixer.servicosapp

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Rule
import org.junit.Test
import java.util.UUID
import java.io.File

class CadastroFlowTest {
    @get:Rule
    val tela = createAndroidComposeRule<MainActivity>()
    private val email = "teste_${UUID.randomUUID()}@example.com"

    @After
    fun limparApenasContaDeTeste() {
        InstrumentationRegistry.getInstrumentation().targetContext
            .getSharedPreferences("cadastros_locais", Context.MODE_PRIVATE)
            .edit().remove("conta:$email").commit()
    }

    private fun abrirCadastro() {
        tela.onNodeWithText("Começar").performClick()
        tela.onNodeWithText("Cadastre-se").performScrollTo().performClick()
    }

    private fun preencherDados() {
        tela.onNodeWithTag("cadastro_nome").performScrollTo().performTextInput("Pessoa de Teste")
        tela.onNodeWithTag("cadastro_email").performScrollTo().performTextInput(email)
        tela.onNodeWithTag("cadastro_senha").performScrollTo().performTextInput("teste123")
    }

    private fun finalizar() {
        tela.onNodeWithText("Finalizar cadastro").performScrollTo().performClick()
        tela.waitUntil(30_000) {
            tela.onAllNodesWithText("Cadastro concluído!").fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun capturar(nome: String) {
        val imagem = tela.onRoot().captureToImage().asAndroidBitmap()
        val contexto = InstrumentationRegistry.getInstrumentation().targetContext
        File(contexto.cacheDir, nome).outputStream().use {
            imagem.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
    }

    @Test
    fun clienteConcluiCadastroEEntraComContaCriada() {
        abrirCadastro()
        preencherDados()
        tela.onNodeWithText("Cliente").performScrollTo().assertIsSelected()
        tela.onNodeWithTag("cadastro_descricao").assertDoesNotExist()
        finalizar()
        tela.onNodeWithText("Cliente").assertIsDisplayed()
        capturar("cadastro-concluido.png")
        tela.onNodeWithText("Ir para login").performScrollTo().performClick()
        tela.onNodeWithTag("login_senha").performScrollTo().performTextInput("teste123")
        tela.onNodeWithText("Entrar →").performScrollTo().performClick()
        tela.waitUntil(30_000) {
            tela.onAllNodesWithText("Minha conta").fetchSemanticsNodes().isNotEmpty()
        }
        tela.onNodeWithText("Pessoa de Teste").assertIsDisplayed()
    }

    @Test
    fun prestadorSelecionaCategoriaEConcluiCadastro() {
        abrirCadastro()
        preencherDados()
        tela.onNodeWithText("Prestador").performScrollTo().performClick().assertIsSelected()
        tela.onNodeWithText("Selecione uma categoria").performScrollTo().performClick()
        tela.onNodeWithText("Eletricista").performClick()
        tela.onNodeWithTag("cadastro_descricao").performScrollTo().performTextInput("Instalações e reparos residenciais")
        tela.onNodeWithText("Prestador").performScrollTo().performClick()
        tela.onNodeWithText("Finalizar cadastro").performScrollTo()
        capturar("cadastro-prestador.png")
        finalizar()
        tela.onNodeWithText("Prestador").assertIsDisplayed()
        tela.onNodeWithText("Eletricista").assertIsDisplayed()
    }

    @Test
    fun formularioInvalidoMostraErroSemConcluir() {
        abrirCadastro()
        tela.onNodeWithText("Finalizar cadastro").performScrollTo().performClick()
        tela.onNodeWithText("Informe seu nome completo.").assertExists()
        tela.onNodeWithText("Cadastro concluído!").assertDoesNotExist()
    }
}
