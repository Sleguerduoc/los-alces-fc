package com.example.losalcesfc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.losalcesfc.data.repository.AuthRepository
import com.example.losalcesfc.navigation.AppNav
import com.example.losalcesfc.ui.theme.SigaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AuthRepository.init(applicationContext)

        setContent {
            SigaTheme {
                AppNav()
            }
        }
    }
}
