package com.example.losalcesfc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.losalcesfc.ui.login.LoginViewModel
import com.example.losalcesfc.ui.login.LoginScreen
import com.example.losalcesfc.ui.theme.SigaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SigaTheme {
                val vm: LoginViewModel = viewModel()
                LoginScreen(
                    viewModel = vm,
                    onLoginSuccess = {
                        // TODO: navegar al Home (NavHost)
                    }
                )
            }
        }
    }
}