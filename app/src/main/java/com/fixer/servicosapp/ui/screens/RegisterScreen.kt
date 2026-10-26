package com.fixer.servicosapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fixer.servicosapp.data.categoriasServico
import com.fixer.servicosapp.data.validarCadastro

private val AzulFixer = Color(0xFF103477)
private val AzulBotao = Color(0xFF1765E8)
private val FundoFixer = Color(0xFFF2F5FA)
private val TextoFixer = Color(0xFF172746)
private val CinzaFixer = Color(0xFF7B879B)
private val BordaFixer = Color(0xFFE4E9F1)

@Composable
fun RegisterScreen(
    onFinalizar: (
        nome: String,
        email: String,
        senha: String,
        tipo: String,
        categoria: String,
        descricao: String,
        telefone: String
    ) -> Unit,
    onJaTenhoConta: () -> Unit = {},
    enviando: Boolean = false,
    erroCadastro: String? = null
) {
    var nome by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var tipo by rememberSaveable { mutableStateOf("Cliente") }
    var categoria by rememberSaveable { mutableStateOf("") }
    var descricao by rememberSaveable { mutableStateOf("") }
    var telefone by rememberSaveable { mutableStateOf("") }
    var mostrarSenha by remember { mutableStateOf(false) }
    var menuAberto by remember { mutableStateOf(false) }
    var erroValidacao by remember { mutableStateOf<String?>(null) }
    val foco = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoFixer)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        //HEADER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(195.dp)
                .background(
                    AzulFixer,
                    RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                )
        ) {
            //retangulo canto sup dir
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(width = 105.dp, height = 58.dp)
                    .background(
                        AzulBotao,
                        RoundedCornerShape(topEnd = 28.dp, bottomStart = 22.dp)
                    )
            )

            Column(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 22.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🔧", fontSize = 19.sp)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "FIXER",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Spacer(Modifier.height(25.dp))

                Text(
                    text = "Crie sua conta para acessar os serviços.",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 10.sp
                )
            }
        }

        //cartao
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 12.dp, end = 12.dp, top = 140.dp, bottom = 12.dp)
                .background( Color.White,
                    RoundedCornerShape(
                        topStart = 22.dp,
                        topEnd = 22.dp,
                        bottomStart = 4.dp,
                        bottomEnd = 4.dp
                    )
                )
                .verticalScroll((rememberScrollState()))
                .padding(horizontal = 20.dp, vertical =  22.dp)
        ) {
            Text(
                text = "Criar conta",
                color = TextoFixer,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(18.dp))

            //nome
            RotuloCampo("Nome Completo:")
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                enabled = !enviando,
                modifier = Modifier.fillMaxWidth().testTag("cadastro_nome"),
                placeholder = { Text("Ex: João Silva", fontSize = 12.sp) },
                leadingIcon = { Text("👤", fontSize = 13.sp) },
                singleLine = true,
                shape = RoundedCornerShape(9.dp),
                colors = coresCampo()
            )

            Spacer(Modifier.height(14.dp))

            //email
            RotuloCampo("Email:")
            OutlinedTextField(
                value = email,
                onValueChange = { email = it},
                enabled = !enviando,
                modifier = Modifier.fillMaxWidth().testTag("cadastro_email"),
                placeholder = { Text("seu@email.com", fontSize = 12.sp) },
                leadingIcon = { Text("✉", fontSize = 13.sp) },
                singleLine = true,
                shape = RoundedCornerShape(9.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = coresCampo()
            )

            Spacer(Modifier.height(14.dp))

            //senha
            RotuloCampo("Senha")
            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it },
                enabled = !enviando,
                modifier = Modifier.fillMaxWidth().testTag("cadastro_senha"),
                placeholder = { Text("Crie uma senha segura", fontSize = 12.sp) },
                leadingIcon = { Text("🔒", fontSize = 13.sp) },
                trailingIcon = {
                    Text(
                        text = if (mostrarSenha) "Ocultar" else "Ver",
                        color = AzulBotao,
                        fontSize = 10.sp,
                        modifier = Modifier
                            .clickable{ mostrarSenha = !mostrarSenha }
                            .padding(6.dp)
                    )
                },
                singleLine = true,
                visualTransformation = if (mostrarSenha) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(9.dp),
                colors = coresCampo()
            )

            Spacer(Modifier.height(14.dp))

            //tipo usuario
            RotuloCampo("Você quer se cadastrar como")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectableGroup(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Cliente", "Prestador").forEach { opcao ->
                    val ativo = opcao == tipo
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 52.dp)
                            .clip(RoundedCornerShape(9.dp))
                            .background(if (ativo) Color(0xFFEAF2FF) else Color.White)
                            .border(1.dp, if (ativo) AzulBotao else BordaFixer, RoundedCornerShape(9.dp))
                            .selectable(
                                selected = ativo,
                                enabled = !enviando,
                                role = Role.RadioButton,
                                onClick = { foco.clearFocus(); tipo = opcao; erroValidacao = null }
                            )
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = ativo,
                            onClick = null,
                            enabled = !enviando,
                            colors = RadioButtonDefaults.colors(selectedColor = AzulBotao)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(opcao, color = if (ativo) AzulBotao else TextoFixer, fontSize = 12.sp)
                    }
                }
            }

            if (tipo == "Prestador") {
                Spacer(Modifier.height(18.dp))
                RotuloCampo("Categoria do serviço")
                Box {
                    OutlinedButton(
                        onClick = { foco.clearFocus(); menuAberto = true },
                        enabled = !enviando,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(9.dp)
                    ) {
                        Text(categoria.ifBlank { "Selecione uma categoria" }, modifier = Modifier.weight(1f))
                        Text("▾")
                    }
                    DropdownMenu(expanded = menuAberto, onDismissRequest = { menuAberto = false }) {
                        categoriasServico.forEach { opcao ->
                            DropdownMenuItem(
                                text = { Text(opcao) },
                                onClick = { categoria = opcao; menuAberto = false }
                            )
                        }
                    }
                }
                Spacer(Modifier.height(14.dp))
                RotuloCampo("Descrição do serviço")
                OutlinedTextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    enabled = !enviando,
                    modifier = Modifier.fillMaxWidth().testTag("cadastro_descricao"),
                    placeholder = { Text("Conte o que você faz e sua experiência", fontSize = 12.sp) },
                    minLines = 3,
                    maxLines = 5,
                    shape = RoundedCornerShape(9.dp),
                    colors = coresCampo()
                )
                Spacer(Modifier.height(14.dp))
                RotuloCampo("Telefone / WhatsApp (com DDD)")
                OutlinedTextField(
                    value = telefone,
                    onValueChange = { telefone = it },
                    enabled = !enviando,
                    modifier = Modifier.fillMaxWidth().testTag("cadastro_telefone"),
                    placeholder = { Text("(11) 99999-9999", fontSize = 12.sp) },
                    supportingText = { Text("Este contato será exibido no seu perfil de prestador.") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    shape = RoundedCornerShape(9.dp),
                    colors = coresCampo()
                )
            }

            Spacer(Modifier.height(18.dp))
            Text("Cadastro salvo neste aparelho para demonstração.", color = CinzaFixer, fontSize = 11.sp)
            val erro = erroValidacao ?: erroCadastro
            if (erro != null) {
                Spacer(Modifier.height(12.dp))
                Text(erro, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    foco.clearFocus()
                    erroValidacao = validarCadastro(nome, email, senha, tipo, categoria, descricao, telefone)
                    if (erroValidacao == null) {
                        onFinalizar(nome.trim(), email.trim(), senha, tipo, categoria, descricao.trim(), telefone)
                    }
                },
                enabled = !enviando,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(9.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AzulBotao)
            ) {
                if (enviando) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                    Spacer(Modifier.width(10.dp))
                }
                Text(if (enviando) "Salvando cadastro…" else "Finalizar cadastro", fontWeight = FontWeight.Bold)
            }
            TextButton(
                onClick = onJaTenhoConta,
                enabled = !enviando,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Já tenho conta. Entrar", color = AzulBotao)
            }
        }

    }
}

@Composable
private fun RotuloCampo(texto: String) {
    Text(
        text = texto,
        color = TextoFixer,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
    )
    Spacer(Modifier.height(6.dp))
}

@Composable
private fun coresCampo() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = AzulBotao,
    unfocusedBorderColor = BordaFixer,
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White
)
