package com.example.losalcesfc.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.losalcesfc.ui.theme.PanelBg
import com.example.losalcesfc.ui.theme.AzulPrimary

@Composable
fun UsuariosMenuScreen(
    onNuevoUsuario: () -> Unit,
    onVerUsuarios: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Gestión de Usuarios", style = MaterialTheme.typography.titleLarge, color = AzulPrimary)
        Spacer(Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = PanelBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                ListItem(
                    headlineContent = { Text("Crear nuevo usuario") },
                    leadingContent = { Icon(Icons.Filled.PersonAdd, contentDescription = null, tint = AzulPrimary) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNuevoUsuario() }
                        .padding(horizontal = 4.dp)
                )
                Divider(thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.4f))
                ListItem(
                    headlineContent = { Text("Ver lista de usuarios") },
                    leadingContent = { Icon(Icons.Filled.List, contentDescription = null, tint = AzulPrimary) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onVerUsuarios() }
                        .padding(horizontal = 4.dp)
                )
            }
        }
    }
}
