
package com.fixer.servicosapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fixer.servicosapp.ui.screens.HomeScreen
import com.fixer.servicosapp.ui.screens.LoginScreen
import com.fixer.servicosapp.ui.screens.RegisterScreen

object Rotas {
    const val INICIO = "inicio"
    const val LOGIN = "login"
    const val CADASTRO = "cadastro"
}

@Composable
fun ServicosAppNavGraph() {
    val navController = rememberNavController()

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
                onIrParaCadastro = {
                    navController.navigate(Rotas.CADASTRO)
                }
            )
        }

        composable(Rotas.CADASTRO) {
            RegisterScreen(
                onVoltarParaLogin = {
                    navController.navigate(Rotas.LOGIN)
                }
            )
        }
    }
}