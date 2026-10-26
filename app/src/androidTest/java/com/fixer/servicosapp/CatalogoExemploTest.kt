package com.fixer.servicosapp

import android.graphics.Bitmap
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.fixer.servicosapp.data.Prestador
import com.fixer.servicosapp.data.prestadoresExemplo
import com.fixer.servicosapp.ui.screens.ProviderDetailScreen
import com.fixer.servicosapp.ui.screens.ProvidersScreen
import com.fixer.servicosapp.ui.theme.ServicosAppTheme
import org.junit.Rule
import org.junit.Test
import java.io.File

class CatalogoExemploTest {
    @get:Rule val tela = createComposeRule()

    private fun capturar(nome: String) {
        val contexto = InstrumentationRegistry.getInstrumentation().targetContext
        File(contexto.cacheDir, nome).outputStream().use {
            tela.onRoot().captureToImage().asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 100, it)
        }
    }

    @Test fun exemploExplicaAusenciaDeContatoSemUsarDadosDeContasLocais() {
        tela.setContent {
            var escolhido by remember { mutableStateOf<Prestador?>(null) }
            val snackbar = remember { SnackbarHostState() }
            ServicosAppTheme {
                if (escolhido == null) ProvidersScreen("Victor", prestadoresExemplo,
                    onSelecionar = { id -> escolhido = prestadoresExemplo.first { it.id == id } }, onConta = {}, onSair = {})
                else ProviderDetailScreen(escolhido, onVoltar = { escolhido = null }, onContato = {}, snackbarHostState = snackbar)
            }
        }
        tela.onNodeWithText("5 resultados").assertExists()
        capturar("catalogo-exemplo.png")
        tela.onNodeWithTag("catalogo_lista").performScrollToNode(hasTestTag("prestador_exemplo:ana"))
        tela.onNodeWithTag("prestador_exemplo:ana").performClick()
        tela.onNodeWithTag("detalhe_nome").assertTextEquals("Ana Martins")
        capturar("detalhes.png")
        tela.onNodeWithTag("contato_whatsapp").performScrollTo().assertIsNotEnabled()
        tela.onNodeWithTag("contato_ligacao").performScrollTo().assertIsNotEnabled()
    }
}
