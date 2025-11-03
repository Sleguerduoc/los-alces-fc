package com.example.losalcesfc.view


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.losalcesfc.ui.theme.PanelBg
import com.example.losalcesfc.ui.theme.AzulPrimary

@Composable
fun AjustesScreen(
    onGestionUsuarios: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text("Ajustes", style = MaterialTheme.typography.titleLarge, color = AzulPrimary)
        Spacer(Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = PanelBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            ListItem(
                headlineContent = { Text("Gestión de usuarios") },
                supportingContent = { Text("Crear, ver y editar usuarios del sistema") },
                leadingContent = { Icon(Icons.Filled.ManageAccounts, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onGestionUsuarios() }
                    .padding(4.dp)
            )
        }
    }
}
