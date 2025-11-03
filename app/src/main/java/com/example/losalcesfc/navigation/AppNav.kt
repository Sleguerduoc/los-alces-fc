package com.example.losalcesfc.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.losalcesfc.ui.login.LoginScreen
import com.example.losalcesfc.ui.login.LoginViewModel
import com.example.losalcesfc.view.DrawerMenu
import com.example.losalcesfc.data.local.SessionPrefs
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

object Routes {
    const val LOGIN = "login"
    const val DRAWER = "drawer"
}

@Composable
fun AppNav() {
    val nav = rememberNavController()
    val context = LocalContext.current
    val session = remember { SessionPrefs.get(context) }
    val scope = rememberCoroutineScope()

    // Observa DataStore
    val isLoggedInFlow = remember { session.isLoggedIn }
    val isLoggedIn by isLoggedInFlow.collectAsState(initial = false)


    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            nav.navigate(Routes.DRAWER) {
                popUpTo(Routes.LOGIN) { inclusive = true }
                launchSingleTop = true
            }
        } else {
            nav.navigate(Routes.LOGIN) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

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
                userName = "Usuario",
                userEmail = null,
                onLogout = {
                    // LIMPIA SESIÓN + NAVEGA (¡sin LaunchedEffect aquí!)
                    scope.launch {
                        session.clear()
                        nav.navigate(Routes.LOGIN) {
                            popUpTo(Routes.DRAWER) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}
