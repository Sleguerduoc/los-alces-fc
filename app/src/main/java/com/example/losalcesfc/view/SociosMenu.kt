package com.example.losalcesfc.view


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.losalcesfc.ui.theme.AzulPrimary
import com.example.losalcesfc.ui.theme.PanelBg

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SociosMenuScreen(
    onNuevoSocio: () -> Unit,
    onVerSocios: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Gestión de Socios",
            style = MaterialTheme.typography.titleLarge,
            color = AzulPrimary,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(16.dp))

        // Tarjeta que contiene los botones tipo menú
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = PanelBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SociosMenuItem(
                    icon = Icons.Filled.PersonAdd,
                    text = "Registrar nuevo socio",
                    onClick = onNuevoSocio
                )
                Divider(thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.4f))
                SociosMenuItem(
                    icon = Icons.Filled.List,
                    text = "Ver lista de socios",
                    onClick = onVerSocios
                )
            }
        }
    }
}

@Composable
private fun SociosMenuItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(text) },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AzulPrimary
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 4.dp)
    )
}
