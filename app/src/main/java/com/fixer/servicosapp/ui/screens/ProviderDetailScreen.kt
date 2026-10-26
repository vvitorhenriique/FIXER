package com.fixer.servicosapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fixer.servicosapp.data.*

@Composable
fun ProviderDetailScreen(prestador: Prestador?, onVoltar: () -> Unit, onContato: (TipoContato) -> Unit, snackbarHostState: SnackbarHostState) {
    Scaffold(containerColor = FixerColors.Background,
        topBar = { FixerTopBar("Profissional", onBack = onVoltar) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding).verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
            if (prestador == null) {
                Text("Profissional não encontrado", style = MaterialTheme.typography.headlineSmall, color = FixerColors.Ink)
                Text("O cadastro pode não estar mais disponível neste aparelho.", color = FixerColors.Muted)
                Button(onClick = onVoltar) { Text("Voltar para profissionais") }
            } else {
                Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        AvatarPrestador(prestador, grande = true)
                        Text(prestador.nome, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = FixerColors.Ink, modifier = Modifier.testTag("detalhe_nome"))
                        Text(prestador.categoria, color = FixerColors.Blue, style = MaterialTheme.typography.titleMedium)
                        AvaliacaoPrestador(prestador)
                        Surface(color = FixerColors.Background, shape = RoundedCornerShape(24.dp)) {
                            Text(if (prestador.exemplo) "Perfil de exemplo" else "Prestador cadastrado neste aparelho", color = FixerColors.Muted,
                                style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp))
                        }
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
                    Text("Sobre o profissional", color = FixerColors.Ink, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(prestador.descricao, color = FixerColors.Muted, style = MaterialTheme.typography.bodyLarge)
                }
                Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Row(Modifier.fillMaxWidth().padding(20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        FixerIcon(FixerSymbol.Phone, tint = FixerColors.Blue)
                        Column {
                            Text("Telefone / WhatsApp", color = FixerColors.Muted, style = MaterialTheme.typography.labelMedium)
                            Text(formatarTelefone(prestador.telefone), color = FixerColors.Ink, style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
                val possuiContato = normalizarTelefone(prestador.telefone) != null
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(onClick = { onContato(TipoContato.WHATSAPP) }, enabled = possuiContato,
                        modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp).testTag("contato_whatsapp"), shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FixerColors.Green)) {
                        FixerIcon(FixerSymbol.Chat)
                        Spacer(Modifier.width(10.dp))
                        Text("Conversar no WhatsApp", fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(onClick = { onContato(TipoContato.LIGACAO) }, enabled = possuiContato,
                        modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp).testTag("contato_ligacao"), shape = RoundedCornerShape(14.dp)) {
                        FixerIcon(FixerSymbol.Phone)
                        Spacer(Modifier.width(10.dp))
                        Text("Ligar para o profissional", fontWeight = FontWeight.Bold)
                    }
                }
                Text(when {
                    prestador.exemplo -> "Este perfil é fictício e não possui telefone. Para testar o contato, cadastre um prestador com um número que você possa usar na apresentação."
                    !possuiContato -> "Este prestador ainda não informou um contato. Ele pode adicionar o telefone em Minha conta."
                    else -> "Combine o serviço e os valores diretamente com o profissional. A ligação abre o discador; você confirma antes de ligar."
                }, color = FixerColors.Muted, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}
