package com.fixer.servicosapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val AzulFixer = Color(0xFF103477)
private val AzulBotao = Color(0xFF1765E8)
private val FundoFixer = Color(0xFFF2F5FA)
private val TextoFixer = Color(0xFF172746)
private val CinzaFixer = Color(0xFF7B879B)


@Composable
fun LoginScreen (
    onEntrar: (String, String) -> Unit = {_, _ -> },
    onCadastrar: () -> Unit = {},
    onEsqueciSenha: () -> Unit = {},
    enviando: Boolean = false,
    erroLogin: String? = null,
    emailInicial: String = ""
) {
    var email by rememberSaveable(emailInicial) { mutableStateOf(emailInicial) }
    var senha by remember { mutableStateOf("") }
    var lembrarDeMim by remember { mutableStateOf(true) }
    var mostrarSenha by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoFixer)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(195.dp)
                .background(
                    AzulFixer,
                    RoundedCornerShape(
                        bottomStart = 28.dp,
                        bottomEnd = 28.dp
                    )
                )
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 22.dp
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                text = "Serviços que facilitam",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(7.dp))

            Text(
                text = "Encontre profissionais de confiança para seu dia a dia.",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 10.sp
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 12.dp,
                    end = 12.dp,
                    top = 140.dp,
                    bottom = 12.dp
                )
                .background(
                    Color.White,
                    RoundedCornerShape(
                        topStart = 22.dp,
                        topEnd = 22.dp,
                        bottomStart = 4.dp,
                        bottomEnd = 4.dp
                    )
                )
                .verticalScroll((rememberScrollState()))
                .padding(horizontal = 20.dp, vertical = 22.dp)
        ) {
            Text(
                text = "Acesse sua conta",
                color = TextoFixer,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Entre para encontrar os profissionais ideais.",
                color = CinzaFixer,
                fontSize = 10.sp
            )

            Spacer(Modifier.height(22.dp))

            Text(
                text = "Email",
                color = TextoFixer,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(7.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth().testTag("login_email"),
                placeholder = {
                    Text("✉  seu@email.com", fontSize = 12.sp)
                },
                singleLine = true,
                shape = RoundedCornerShape(9.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AzulBotao,
                    unfocusedBorderColor = Color(0xFFE4E9F1),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Senha",
                color = TextoFixer,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(7.dp))

            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it },
                modifier = Modifier.fillMaxWidth().testTag("login_senha"),
                placeholder = {
                    Text("Digite sua senha", fontSize = 12.sp)
                },
                leadingIcon = {
                    Text("🔒", fontSize = 13.sp)
                },
                trailingIcon = {
                    Text(
                        text = if (mostrarSenha) "Ocultar"
                        else "Ver",
                        color = AzulBotao,
                        fontSize = 10.sp,
                        modifier = Modifier
                            .clickable {
                                mostrarSenha = !mostrarSenha
                            }
                            .padding(6.dp)
                    )
                },
                singleLine = true,
                visualTransformation = if (mostrarSenha) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                shape = RoundedCornerShape(9.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AzulBotao,
                    unfocusedBorderColor = Color(0xFFE4E9F1),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = lembrarDeMim,
                    onCheckedChange = { lembrarDeMim = it },
                    modifier = Modifier.size(22.dp),
                    colors = CheckboxDefaults.colors(
                        checkedColor = AzulBotao
                    )
                )

                Spacer(Modifier.width(4.dp))

                Text(
                    text = "Lembrar de mim",
                    color = CinzaFixer,
                    fontSize = 10.sp
                )

                Spacer(Modifier.weight(1f))

                Text(
                    text = "Esqueceu a senha?",
                    color = AzulBotao,
                    fontSize = 10.sp,
                    modifier = Modifier.clickable {
                        onEsqueciSenha()
                    }
                )
            }

            Spacer(Modifier.height(15.dp))

            if (erroLogin != null) {
                Text(erroLogin, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                Spacer(Modifier.height(12.dp))
            }
            Button(
                onClick = {
                    onEntrar(email.trim(), senha)
                },
                enabled = !enviando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(9.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulBotao
                )
            ) {
                Text(
                    text = if (enviando) "Entrando…" else "Entrar →",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ainda não tem uma conta? ",
                    color = CinzaFixer,
                    fontSize = 10.sp
                )

                Text(
                    text = "Cadastre-se",
                    color = AzulBotao,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        onCadastrar()
                    }
                )
            }

            Spacer(Modifier.height(24.dp))

            // Cartão de benefícios
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFF2F6FD),
                        RoundedCornerShape(13.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "Por que usar o FIXER?",
                    color = TextoFixer,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = "✓  Profissionais avaliados",
                color = AzulFixer,
                fontSize = 10.sp
            )

            Spacer(Modifier.height(7.dp))

            Text(
                text = "✓  Contrate direto e com segurança",
                color = AzulFixer,
                fontSize = 10.sp
            )
        }

        Spacer(Modifier.height(20.dp))

    }
}
