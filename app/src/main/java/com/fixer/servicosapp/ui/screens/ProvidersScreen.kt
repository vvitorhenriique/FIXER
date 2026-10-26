package com.fixer.servicosapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.fixer.servicosapp.data.*

@Composable
fun ProvidersScreen(nome: String, prestadores: List<Prestador>, onSelecionar: (String) -> Unit, onConta: () -> Unit, onSair: () -> Unit) {
    var busca by rememberSaveable { mutableStateOf("") }
    var categoria by rememberSaveable { mutableStateOf("Todos") }
    val foco = LocalFocusManager.current
    val filtrados = remember(prestadores, busca, categoria) { filtrarPrestadores(prestadores, busca, categoria) }
    Scaffold(containerColor = FixerColors.Background,
        topBar = { FixerTopBar(onConta = onConta, onSair = onSair) },
        bottomBar = { FixerBottomBar(false, {}, onConta) }
    ) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding).imePadding().testTag("catalogo_lista"),
            contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                Text("Olá, ${nome.substringBefore(' ')}", color = FixerColors.Muted, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                Text("Encontre quem\nresolve para você.", color = FixerColors.Ink, fontSize = 29.sp, lineHeight = 35.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text("Da manutenção à limpeza, seu próximo cuidado começa aqui.", color = FixerColors.Muted, style = MaterialTheme.typography.bodyMedium)
            }
            item {
                OutlinedTextField(value = busca, onValueChange = { busca = it },
                    modifier = Modifier.fillMaxWidth().testTag("busca_prestadores"),
                    label = { Text("Buscar profissional") }, placeholder = { Text("Nome ou categoria") },
                    leadingIcon = { FixerIcon(FixerSymbol.Search) },
                    trailingIcon = if (busca.isNotEmpty()) { { TextButton(onClick = { busca = "" }) { Text("Limpar") } } } else null,
                    singleLine = true, shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { foco.clearFocus() }),
                    colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White, focusedContainerColor = Color.White, unfocusedBorderColor = FixerColors.Line))
            }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(listOf("Todos") + categoriasServico) { opcao ->
                        FilterChip(selected = categoria == opcao, onClick = { foco.clearFocus(); categoria = opcao }, label = { Text(opcao) },
                            modifier = Modifier.testTag("filtro_$opcao"), shape = RoundedCornerShape(24.dp))
                    }
                }
            }
            item {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("Profissionais", color = FixerColors.Ink, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Text("${filtrados.size} ${if (filtrados.size == 1) "resultado" else "resultados"}", color = FixerColors.Muted, style = MaterialTheme.typography.bodySmall)
                }
            }
            if (filtrados.isEmpty()) {
                item {
                    Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(20.dp)) {
                        Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            FixerIcon(FixerSymbol.Search, tint = FixerColors.Blue)
                            Text("Nenhum profissional encontrado", fontWeight = FontWeight.Bold, color = FixerColors.Ink)
                            Text("Tente outro nome ou escolha uma categoria diferente.", color = FixerColors.Muted)
                            OutlinedButton(onClick = { busca = ""; categoria = "Todos" }) { Text("Limpar filtros") }
                        }
                    }
                }
            }
            items(filtrados, key = { it.id }) { prestador ->
                PrestadorCard(prestador, onClick = { foco.clearFocus(); onSelecionar(prestador.id) })
            }
            item {
                Text("Os perfis com selo “Exemplo” e suas notas são fictícios. Novos prestadores cadastrados neste aparelho aparecem aqui, sem avaliações.", color = FixerColors.Muted, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun PrestadorCard(prestador: Prestador, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth().testTag("prestador_${prestador.id}"),
        shape = RoundedCornerShape(20.dp), border = BorderStroke(1.dp, FixerColors.Line),
        colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                AvatarPrestador(prestador)
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(prestador.nome, color = FixerColors.Ink, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    Text(prestador.categoria, color = FixerColors.Blue, style = MaterialTheme.typography.labelLarge)
                    AvaliacaoPrestador(prestador)
                }
            }
            Text(prestador.descricao, color = FixerColors.Muted, style = MaterialTheme.typography.bodyMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
            HorizontalDivider(color = FixerColors.Line)
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(if (prestador.exemplo) "Exemplo" else "Cadastro local", color = FixerColors.Muted, style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(1f))
                Text("Ver profissional", color = FixerColors.Blue, style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.width(8.dp))
                FixerIcon(FixerSymbol.Arrow, tint = FixerColors.Blue)
            }
        }
    }
}
