package com.fixer.servicosapp.ui.navigation

import android.content.ActivityNotFoundException
import android.app.Activity
import android.net.Uri
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.core.view.WindowCompat
import androidx.navigation.navArgument
import com.fixer.servicosapp.data.*
import com.fixer.servicosapp.ui.screens.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object Rotas {
    const val INICIO = "inicio"
    const val LOGIN = "login"
    const val CADASTRO = "cadastro"
    const val CONCLUIDO = "cadastro_concluido"
    const val CONTA = "conta"
    const val PROFISSIONAIS = "profissionais"
    const val DETALHE = "profissional/{id}"
}

@Composable
fun ServicosAppNavGraph() {
    val navController = rememberNavController()
    val contexto = LocalContext.current
    val foco = LocalFocusManager.current
    val entradaAtual by navController.currentBackStackEntryAsState()
    SideEffect {
        (contexto as? Activity)?.let { activity ->
            WindowCompat.getInsetsController(activity.window, activity.window.decorView).isAppearanceLightStatusBars =
                entradaAtual?.destination?.route !in setOf(Rotas.PROFISSIONAIS, Rotas.DETALHE, Rotas.CONTA, Rotas.CONCLUIDO)
        }
    }
    val cadastroLocal = remember { CadastroLocal(contexto) }
    val escopo = rememberCoroutineScope()
    var salvando by remember { mutableStateOf(false) }
    var entrando by remember { mutableStateOf(false) }
    var salvandoContato by remember { mutableStateOf(false) }
    var erroCadastro by remember { mutableStateOf<String?>(null) }
    var erroLogin by remember { mutableStateOf<String?>(null) }
    var erroContato by remember { mutableStateOf<String?>(null) }
    var mensagemContato by remember { mutableStateOf<String?>(null) }
    var emailAutenticado by rememberSaveable { mutableStateOf(cadastroLocal.emailDaSessao()) }
    var emailCadastrado by rememberSaveable { mutableStateOf("") }
    var versaoPerfil by remember { mutableIntStateOf(0) }
    val perfil = remember(emailAutenticado, versaoPerfil) { emailAutenticado?.let(cadastroLocal::obterPerfil) }
    val rotaInicial = remember { if (perfil != null) Rotas.PROFISSIONAIS else Rotas.INICIO }
    val prestadores = remember(emailAutenticado, emailCadastrado, versaoPerfil) { cadastroLocal.prestadores() + prestadoresExemplo }

    fun sair() {
        foco.clearFocus()
        cadastroLocal.manterSessao(null)
        emailAutenticado = null
        erroLogin = null
        erroContato = null
        mensagemContato = null
        navController.navigate(Rotas.LOGIN) { popUpTo(navController.graph.id) { inclusive = true }; launchSingleTop = true }
    }
    fun explorar() {
        foco.clearFocus()
        navController.navigate(Rotas.PROFISSIONAIS) { popUpTo(Rotas.PROFISSIONAIS); launchSingleTop = true; restoreState = true }
    }

    NavHost(navController, startDestination = rotaInicial) {
        composable(Rotas.INICIO) { HomeScreen(onComecar = { navController.navigate(Rotas.LOGIN) }) }
        composable(Rotas.LOGIN) {
            LoginScreen(enviando = entrando, erroLogin = erroLogin, emailInicial = emailCadastrado,
                onEntrar = { email, senha, lembrar ->
                    if (!entrando) {
                        foco.clearFocus()
                        entrando = true
                        erroLogin = null
                        escopo.launch(Dispatchers.Main.immediate) {
                            val resultado = withContext(Dispatchers.IO) { cadastroLocal.entrar(email, senha) }
                            entrando = false
                            resultado.onSuccess {
                                emailAutenticado = it.email
                                cadastroLocal.manterSessao(if (lembrar) it.email else null)
                                navController.navigate(Rotas.PROFISSIONAIS) { popUpTo(navController.graph.id) { inclusive = true }; launchSingleTop = true }
                            }.onFailure { erroLogin = it.message ?: "Não foi possível entrar. Tente novamente." }
                        }
                    }
                },
                onEsqueciSenha = { erroLogin = "Esta versão usa contas locais. A recuperação de senha por e-mail ainda não está disponível." },
                onCadastrar = { erroCadastro = null; navController.navigate(Rotas.CADASTRO) })
        }
        composable(Rotas.CADASTRO) {
            RegisterScreen(enviando = salvando, erroCadastro = erroCadastro,
                onFinalizar = { nome, email, senha, tipo, categoria, descricao, telefone ->
                    if (!salvando) {
                        salvando = true
                        erroCadastro = null
                        escopo.launch(Dispatchers.Main.immediate) {
                            val resultado = withContext(Dispatchers.IO) { cadastroLocal.cadastrar(nome, email, senha, tipo, categoria, descricao, telefone) }
                            salvando = false
                            resultado.onSuccess {
                                emailCadastrado = it.email
                                navController.navigate(Rotas.CONCLUIDO) { popUpTo(Rotas.LOGIN) { inclusive = true } }
                            }.onFailure { erroCadastro = it.message ?: "Não foi possível salvar o cadastro." }
                        }
                    }
                }, onJaTenhoConta = { erroLogin = null; navController.popBackStack(Rotas.LOGIN, false) })
        }
        composable(Rotas.CONCLUIDO) {
            val novoPerfil = cadastroLocal.obterPerfil(emailCadastrado)
            if (novoPerfil != null) AccountScreen(novoPerfil, true, onSair = ::sair)
            else LaunchedEffect(Unit) { sair() }
        }
        composable(Rotas.PROFISSIONAIS) {
            if (perfil != null) ProvidersScreen(perfil.nome, prestadores,
                onSelecionar = { navController.navigate("profissional/${Uri.encode(it)}") },
                onConta = { navController.navigate(Rotas.CONTA) { launchSingleTop = true } }, onSair = ::sair)
            else LaunchedEffect(Unit) { sair() }
        }
        composable(Rotas.CONTA) {
            if (perfil != null) AccountScreen(perfil, false, onSair = ::sair, onExplorar = ::explorar,
                salvandoContato = salvandoContato, mensagemContato = mensagemContato, erroContato = erroContato,
                onSalvarContato = { telefone ->
                    if (!salvandoContato) {
                        foco.clearFocus()
                        salvandoContato = true
                        erroContato = null
                        mensagemContato = null
                        escopo.launch(Dispatchers.Main.immediate) {
                            val resultado = withContext(Dispatchers.IO) { cadastroLocal.atualizarTelefone(perfil.email, telefone) }
                            salvandoContato = false
                            resultado.onSuccess { versaoPerfil++; mensagemContato = "Contato atualizado!" }
                                .onFailure { erroContato = it.message ?: "Não foi possível salvar o contato." }
                        }
                    }
                })
            else LaunchedEffect(Unit) { sair() }
        }
        composable(Rotas.DETALHE, arguments = listOf(navArgument("id") { type = NavType.StringType })) { entrada ->
            val prestador = prestadores.find { it.id == entrada.arguments?.getString("id") }
            val snackbar = remember { SnackbarHostState() }
            if (perfil != null) ProviderDetailScreen(prestador, onVoltar = { navController.popBackStack() }, snackbarHostState = snackbar,
                onContato = { tipo ->
                    prestador?.let { intentDeContato(it, tipo) }?.let { intent ->
                        try { contexto.startActivity(intent) }
                        catch (_: ActivityNotFoundException) { escopo.launch(Dispatchers.Main.immediate) { snackbar.showSnackbar("Nenhum aplicativo disponível para abrir este contato.") } }
                        catch (_: SecurityException) { escopo.launch(Dispatchers.Main.immediate) { snackbar.showSnackbar("Não foi possível abrir o contato neste aparelho.") } }
                    }
                })
            else LaunchedEffect(Unit) { sair() }
        }
    }
}
