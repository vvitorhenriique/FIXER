
package com.fixer.servicosapp.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fixer.servicosapp.ui.screens.HomeScreen
import com.fixer.servicosapp.ui.screens.LoginScreen
import com.fixer.servicosapp.ui.screens.RegisterScreen
import com.fixer.servicosapp.ui.screens.AccountScreen
import com.fixer.servicosapp.data.CadastroLocal
import com.fixer.servicosapp.data.PerfilLocal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object Rotas {
    const val INICIO = "inicio"
    const val LOGIN = "login"
    const val CADASTRO = "cadastro"
    const val CONCLUIDO = "cadastro_concluido"
    const val CONTA = "conta"
}

@Composable
fun ServicosAppNavGraph() {
    val navController = rememberNavController()
    val contexto = LocalContext.current
    val cadastroLocal = remember { CadastroLocal(contexto) }
    val escopo = rememberCoroutineScope()
    var salvando by remember { mutableStateOf(false) }
    var entrando by remember { mutableStateOf(false) }
    var erroCadastro by remember { mutableStateOf<String?>(null) }
    var erroLogin by remember { mutableStateOf<String?>(null) }
    var perfil by remember { mutableStateOf<PerfilLocal?>(null) }
    var emailCadastrado by remember { mutableStateOf("") }

    NavHost(
        navController = navController,
        startDestination = Rotas.INICIO
    ) {
        composable(Rotas.INICIO) {
            HomeScreen(
                onComecar = {
                    navController.navigate(Rotas.LOGIN)
                }
            )
        }

        composable(Rotas.LOGIN) {
            LoginScreen(
                enviando = entrando,
                erroLogin = erroLogin,
                emailInicial = emailCadastrado,
                onEntrar = { email, senha ->
                    if (!entrando) {
                        entrando = true
                        erroLogin = null
                        escopo.launch(Dispatchers.Main.immediate) {
                            val resultado = withContext(Dispatchers.IO) { cadastroLocal.entrar(email, senha) }
                            entrando = false
                            resultado.onSuccess {
                                perfil = it
                                navController.navigate(Rotas.CONTA) {
                                    popUpTo(Rotas.LOGIN) { inclusive = true }
                                }
                            }.onFailure { erroLogin = it.message ?: "Não foi possível entrar. Tente novamente." }
                        }
                    }
                },
                onEsqueciSenha = {
                    erroLogin = "Esta versão usa contas locais. A recuperação de senha por e-mail ainda não está disponível."
                },
                onCadastrar = {
                    erroCadastro = null
                    navController.navigate(Rotas.CADASTRO)
                }
            )
        }

        composable(Rotas.CADASTRO) {
            RegisterScreen(
                enviando = salvando,
                erroCadastro = erroCadastro,
                onFinalizar = { nome, email, senha, tipo, categoria, descricao ->
                    if (!salvando) {
                        salvando = true
                        erroCadastro = null
                        escopo.launch(Dispatchers.Main.immediate) {
                            val resultado = withContext(Dispatchers.IO) {
                                cadastroLocal.cadastrar(nome, email, senha, tipo, categoria, descricao)
                            }
                            salvando = false
                            resultado.onSuccess {
                                perfil = it
                                emailCadastrado = it.email
                                navController.navigate(Rotas.CONCLUIDO) {
                                    popUpTo(Rotas.LOGIN) { inclusive = true }
                                }
                            }.onFailure { erroCadastro = it.message ?: "Não foi possível salvar o cadastro." }
                        }
                    }
                },
                onJaTenhoConta = {
                    erroLogin = null
                    navController.popBackStack(Rotas.LOGIN, inclusive = false)
                }
            )
        }
        listOf(Rotas.CONCLUIDO, Rotas.CONTA).forEach { rota ->
            composable(rota) {
                val perfilAtual = perfil
                if (perfilAtual != null) {
                    AccountScreen(
                        perfil = perfilAtual,
                        cadastroConcluido = rota == Rotas.CONCLUIDO,
                        onSair = {
                            perfil = null
                            erroLogin = null
                            navController.navigate(Rotas.LOGIN) {
                                popUpTo(Rotas.INICIO)
                                launchSingleTop = true
                            }
                        }
                    )
                } else {
                    LaunchedEffect(Unit) {
                        navController.navigate(Rotas.LOGIN) {
                            popUpTo(Rotas.INICIO)
                            launchSingleTop = true
                        }
                    }
                }
            }
        }
    }
}
