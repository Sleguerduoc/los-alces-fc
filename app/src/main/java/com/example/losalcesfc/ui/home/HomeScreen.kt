package com.example.losalcesfc.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.losalcesfc.R
import com.example.losalcesfc.ui.theme.AzulPrimary
import com.example.losalcesfc.ui.theme.DoradoAccent
import com.example.losalcesfc.ui.theme.PanelBg
import com.example.losalcesfc.ui.theme.TextoAzul
import com.example.losalcesfc.ui.theme.AppCornerRadius

@Composable
fun HomeScreen(
    userName: String,
    userEmail: String? = null,
    onComenzar: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(AzulPrimary),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AzulPrimary)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(AppCornerRadius))
                    .background(PanelBg)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo (igual que en el login)
                Image(
                    painter = painterResource(id = R.drawable.logo_duoc_alces),
                    contentDescription = "Logo Los Alces F.C.",
                    modifier = Modifier.size(96.dp)
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Bienvenido a SIGA – Los Alces F.C.",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextoAzul,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                // Información del usuario
                Text(
                    text = userName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextoAzul,
                    textAlign = TextAlign.Center
                )
                userEmail?.let {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoAzul.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(Modifier.height(24.dp))

                // Botón dorado (mismo estilo del login)
                Button(
                    onClick = onComenzar,
                    shape = RoundedCornerShape(AppCornerRadius),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DoradoAccent,
                        contentColor = Color(0xFF1B1B1B)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("Comenzar", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
