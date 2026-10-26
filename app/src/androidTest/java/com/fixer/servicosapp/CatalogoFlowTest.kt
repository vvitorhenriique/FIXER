package com.fixer.servicosapp

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.fixer.servicosapp.data.CadastroLocal
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExternalResource
import org.junit.rules.RuleChain
import java.io.File
import java.util.UUID

class CatalogoFlowTest {
    private val tela = createAndroidComposeRule<MainActivity>()
    private val contexto get() = InstrumentationRegistry.getInstrumentation().targetContext
    private val email = "catalogo_${UUID.randomUUID()}@example.com"
    private var sessaoAnterior: String? = null
    private val preparar = object : ExternalResource() {
        override fun before() {
            val contas = CadastroLocal(contexto)
            sessaoAnterior = contas.emailDaSessao()
            contas.cadastrar("Victor", email, "teste123", "Prestador", "Eletricista", "Instalações e reparos elétricos residenciais.", "11987654321").getOrThrow()
            contas.manterSessao(email)
        }
        override fun after() {
            CadastroLocal(contexto).manterSessao(sessaoAnterior)
            contexto.getSharedPreferences("cadastros_locais", Context.MODE_PRIVATE).edit().remove("conta:$email").commit()
        }
    }
    @get:Rule val regras: RuleChain = RuleChain.outerRule(preparar).around(tela)

    private fun capturar(nome: String) {
        val imagem = tela.onRoot().captureToImage().asAndroidBitmap()
        File(contexto.cacheDir, nome).outputStream().use { imagem.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    @Test fun buscaFiltroDetalheEVoltaPreservamPesquisa() {
        tela.onNodeWithTag("busca_prestadores").assertExists()
        capturar("catalogo.png")
        tela.onNodeWithTag("busca_prestadores").performTextInput("ana")
        tela.onNodeWithTag("busca_prestadores").performImeAction()
        tela.onNodeWithTag("filtro_Eletricista").performScrollTo().performClick()
        tela.onNodeWithTag("catalogo_lista").performScrollToNode(hasTestTag("prestador_exemplo:ana"))
        tela.onNodeWithTag("prestador_exemplo:ana").performClick()
        tela.onNodeWithTag("detalhe_nome").assertTextEquals("Ana Martins")
        tela.onNodeWithTag("contato_whatsapp").performScrollTo().assertIsNotEnabled()
        tela.onNodeWithTag("contato_ligacao").performScrollTo().assertIsNotEnabled()
        tela.onNodeWithContentDescription("Voltar").performClick()
        tela.onNodeWithTag("busca_prestadores").performScrollTo().assertTextContains("ana")
        tela.onNodeWithTag("busca_prestadores").performTextReplacement("semresultado123")
        tela.onNodeWithTag("busca_prestadores").performImeAction()
        tela.onNodeWithTag("catalogo_lista").performScrollToNode(hasText("Nenhum profissional encontrado"))
        tela.onNodeWithText("Nenhum profissional encontrado").assertIsDisplayed()
        tela.onNodeWithText("Limpar filtros").performClick()
        tela.onNodeWithTag("busca_prestadores").assertTextContains("")
        tela.onNodeWithTag("filtro_Todos").assertIsSelected()
    }

    @Test fun prestadorLocalTemContatoERestauraDetalheAposRecriacao() {
        tela.onNodeWithTag("catalogo_lista").performScrollToNode(hasTestTag("prestador_local:$email"))
        tela.onNodeWithTag("prestador_local:$email").performClick()
        tela.onNodeWithTag("detalhe_nome").assertTextEquals("Victor")
        tela.onNodeWithTag("contato_whatsapp").performScrollTo().assertIsEnabled()
        tela.onNodeWithTag("contato_ligacao").performScrollTo().assertIsEnabled()
        tela.activityRule.scenario.recreate()
        tela.onNodeWithTag("detalhe_nome").assertTextEquals("Victor")
        tela.onNodeWithContentDescription("Voltar").performClick()
        tela.onNodeWithContentDescription("Sair da conta").performClick()
        tela.onNodeWithTag("login_email").assertExists()
        assertNull(CadastroLocal(contexto).emailDaSessao())
    }

    @Test fun atualizaContatoNaContaERefleteNosDetalhes() {
        tela.onNodeWithContentDescription("Abrir minha conta").performClick()
        tela.onNodeWithTag("conta_telefone").performScrollTo().performTextReplacement("41987654321")
        tela.onNodeWithText("Salvar contato").performScrollTo().performClick()
        tela.waitUntil(10_000) { tela.onAllNodesWithText("Contato atualizado!").fetchSemanticsNodes().isNotEmpty() }
        tela.onNodeWithText("Explorar profissionais").performScrollTo().performClick()
        tela.waitUntil(10_000) { tela.onAllNodesWithTag("catalogo_lista").fetchSemanticsNodes().isNotEmpty() }
        tela.onNodeWithTag("catalogo_lista").performScrollToNode(hasTestTag("prestador_local:$email"))
        tela.onNodeWithTag("prestador_local:$email").performClick()
        tela.onNodeWithText("(41) 98765-4321").performScrollTo().assertIsDisplayed()
    }

}
