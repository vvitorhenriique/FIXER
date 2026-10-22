package com.fixer.servicosapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fixer.servicosapp.data.PerfilLocal

@Composable
fun AccountScreen(perfil: PerfilLocal, cadastroConcluido: Boolean, onSair: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF2F5FA))
            .safeDrawingPadding().verticalScroll(rememberScrollState()).padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("FIXER", color = Color(0xFF103477), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(16.dp))
        Text(
            if (cadastroConcluido) "Cadastro concluído!" else "Minha conta",
            color = Color(0xFF172746), style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            if (cadastroConcluido) "Tudo pronto, ${perfil.nome}! Confira seu cadastro abaixo."
            else "Olá, ${perfil.nome}! Você entrou na sua conta.",
            color = Color(0xFF58667C)
        )
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                CampoPerfil("Nome", perfil.nome)
                CampoPerfil("E-mail", perfil.email)
                CampoPerfil("Tipo de conta", perfil.tipo)
                if (perfil.tipo == "Prestador") {
                    CampoPerfil("Categoria", perfil.categoria)
                    CampoPerfil("Descrição", perfil.descricao)
                }
            }
        }
        Text("Conta de demonstração salva somente neste aparelho.", style = MaterialTheme.typography.bodySmall)
        Button(
            onClick = onSair,
            modifier = Modifier.fillMaxWidth().heightIn(min = 50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1765E8)),
            shape = RoundedCornerShape(9.dp)
        ) {
            Text(if (cadastroConcluido) "Ir para login" else "Sair")
        }
    }
}

@Composable
private fun CampoPerfil(rotulo: String, valor: String) {
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(rotulo, color = Color(0xFF7B879B), style = MaterialTheme.typography.labelMedium)
        Text(valor, color = Color(0xFF172746), style = MaterialTheme.typography.bodyLarge)
    }
}
