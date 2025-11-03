package com.example.losalcesfc.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.losalcesfc.ui.theme.AzulPrimary
import com.example.losalcesfc.ui.theme.DoradoAccent
import com.example.losalcesfc.ui.theme.PanelBg

@Composable
fun DashboardContent(
    onNuevoSocio: () -> Unit,
    onVerSocios: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Encabezado
        Text(
            text = "Panel Principal",
            style = MaterialTheme.typography.titleLarge,
            color = AzulPrimary,
            fontWeight = FontWeight.SemiBold
        )

        // Tarjeta de resumen
        Card(
            colors = CardDefaults.cardColors(containerColor = PanelBg),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Resumen del Club", color = AzulPrimary, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                Text("Socios activos: 124")
                Text("Equipos registrados: 4")
                Text("Próximo partido: Domingo 18:00")
            }
        }

        // Accesos rápidos
        Card(
            colors = CardDefaults.cardColors(containerColor = DoradoAccent.copy(alpha = 0.15f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Accesos rápidos", color = AzulPrimary, fontWeight = FontWeight.SemiBold)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onNuevoSocio,
                        modifier = Modifier.weight(1f)
                    ) { Text("Registrar socio") }

                    Button(
                        onClick = onVerSocios,
                        modifier = Modifier.weight(1f)
                    ) { Text("Ver socios") }
                }
            }
        }
    }
}
