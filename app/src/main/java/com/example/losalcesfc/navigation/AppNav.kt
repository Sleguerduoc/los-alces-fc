package com.example.losalcesfc.navigation

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.losalcesfc.ui.login.LoginScreen
import com.example.losalcesfc.ui.login.LoginViewModel
import com.example.losalcesfc.ui.home.HomeScreen
import com.example.losalcesfc.view.DrawerMenu
import com.example.losalcesfc.data.repository.AuthRepository
import kotlinx.coroutines.flow.collectLatest

object Routes {
    const val LOGIN = "login"
    const val HOME_WELCOME = "home_welcome"
    const val DRAWER = "drawer"
}

@Composable
fun AppNav() {
    val nav = rememberNavController()

    // Control de sesión: si ya hay sesión activa, salta al HOME directamente
    var bootToWelcome by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        AuthRepository.instance.isLoggedIn().collectLatest { logged ->
            if (logged) bootToWelcome = true
        }
    }
    LaunchedEffect(bootToWelcome) {
        if (bootToWelcome) {
            nav.navigate(Routes.HOME_WELCOME) {
                popUpTo(Routes.LOGIN) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    // Configuración principal de navegación
    NavHost(
        navController = nav,
        startDestination = Routes.LOGIN
    ) {
        // --- LOGIN ---
        composable(Routes.LOGIN) {
            val vm: LoginViewModel = viewModel()
            LoginScreen(
                viewModel = vm,
                onLoginSuccess = {
                    nav.navigate(Routes.HOME_WELCOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // --- HOME DE BIENVENIDA ---
        composable(Routes.HOME_WELCOME) {
            HomeScreen(
                userName = "Usuario Prueba",
                userEmail = "admin@alces.cl",
                onComenzar = {
                    nav.navigate(Routes.DRAWER) {
                        popUpTo(Routes.HOME_WELCOME) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }

        // --- DRAWER (menú lateral con navegación interna) ---
        composable(Routes.DRAWER) {
            DrawerMenu(
                userName = "Usuario Prueba",
                userEmail = "admin@alces.cl",
                onLogout = {
                    // Cierra sesión y vuelve al login
                    nav.navigate(Routes.LOGIN) {
                        popUpTo(Routes.DRAWER) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
