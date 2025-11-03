package com.example.losalcesfc.navigation

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.losalcesfc.ui.login.LoginScreen
import com.example.losalcesfc.ui.login.LoginViewModel
import com.example.losalcesfc.view.DrawerMenu

object Routes {
    const val LOGIN = "login"
    const val DRAWER = "drawer"
}

@Composable
fun AppNav() {
    val nav = rememberNavController()

    NavHost(
        navController = nav,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            val vm: LoginViewModel = viewModel()
            LoginScreen(
                viewModel = vm,
                onLoginSuccess = {
                    nav.navigate(Routes.DRAWER) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.DRAWER) {
            DrawerMenu(
                userName = "Usuario Prueba",
                userEmail = "admin@alces.cl",
                onLogout = {
                    nav.navigate(Routes.LOGIN) {
                        popUpTo(Routes.DRAWER) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
