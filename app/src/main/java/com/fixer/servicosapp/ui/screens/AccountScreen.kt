package com.fixer.servicosapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.fixer.servicosapp.data.PerfilLocal
import com.fixer.servicosapp.data.formatarTelefone

@Composable
fun AccountScreen(
    perfil: PerfilLocal, cadastroConcluido: Boolean, onSair: () -> Unit,
    onExplorar: () -> Unit = {}, onSalvarContato: (String) -> Unit = {},
    salvandoContato: Boolean = false, mensagemContato: String? = null, erroContato: String? = null
) {
    var telefone by rememberSaveable(perfil.telefone) { mutableStateOf(perfil.telefone) }
    Scaffold(containerColor = FixerColors.Background,
        topBar = { FixerTopBar(if (cadastroConcluido) "FIXER" else "Minha conta", onSair = if (cadastroConcluido) null else onSair) },
        bottomBar = { if (!cadastroConcluido) FixerBottomBar(true, onExplorar, {}) }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding).imePadding().verticalScroll(rememberScrollState()).padding(24.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Text(if (cadastroConcluido) "Cadastro concluído!" else "Seu perfil no FIXER", color = FixerColors.Ink, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(if (cadastroConcluido) "Tudo pronto, ${perfil.nome}! Confira seus dados e entre para explorar os serviços."
                else "Olá, ${perfil.nome}! Confira os dados da sua conta.", color = FixerColors.Muted)
            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    CampoPerfil("Nome", perfil.nome)
                    CampoPerfil("E-mail", perfil.email)
                    CampoPerfil("Tipo de conta", perfil.tipo)
                    if (perfil.tipo == "Prestador") {
                        CampoPerfil("Categoria", perfil.categoria)
                        CampoPerfil("Descrição", perfil.descricao)
                        CampoPerfil("Telefone / WhatsApp", formatarTelefone(perfil.telefone))
                    }
                }
            }
            if (perfil.tipo == "Prestador" && !cadastroConcluido) {
                Text("Contato do seu serviço", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = FixerColors.Ink)
                Text("Seu perfil aparece no catálogo deste aparelho. Mantenha o telefone atualizado para receber contatos.", color = FixerColors.Muted)
                OutlinedTextField(value = telefone, onValueChange = { telefone = it }, enabled = !salvandoContato,
                    label = { Text("Telefone com DDD") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("conta_telefone"),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), shape = RoundedCornerShape(14.dp))
                erroContato?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                mensagemContato?.let { Text(it, color = FixerColors.Green) }
                OutlinedButton(onClick = { onSalvarContato(telefone) }, enabled = !salvandoContato, modifier = Modifier.fillMaxWidth().heightIn(min = 50.dp)) {
                    Text(if (salvandoContato) "Salvando contato…" else "Salvar contato")
                }
            }
            Text("Conta de demonstração salva somente neste aparelho.", color = FixerColors.Muted, style = MaterialTheme.typography.bodySmall)
            Button(onClick = if (cadastroConcluido) onSair else onExplorar,
                modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp), shape = RoundedCornerShape(14.dp)) {
                Text(if (cadastroConcluido) "Ir para login" else "Explorar profissionais")
            }
            if (!cadastroConcluido) TextButton(onClick = onSair, modifier = Modifier.fillMaxWidth()) { Text("Sair da conta") }
        }
    }
}

@Composable
private fun CampoPerfil(rotulo: String, valor: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(rotulo, color = FixerColors.Muted, style = MaterialTheme.typography.labelMedium)
        Text(valor, color = FixerColors.Ink, style = MaterialTheme.typography.bodyLarge)
    }
}
