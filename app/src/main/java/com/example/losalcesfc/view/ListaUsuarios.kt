
@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.example.losalcesfc.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.losalcesfc.data.model.Usuario
import com.example.losalcesfc.viewmodel.UsuarioViewModel
import com.example.losalcesfc.ui.theme.PanelBg
import com.example.losalcesfc.ui.theme.AzulPrimary
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider


@Composable
fun ListaUsuariosScreen(
    onBack: () -> Unit,
    onEditarUsuario: (Usuario) -> Unit = {},
    vm: UsuarioViewModel = viewModel()
) {
    val lista by vm.usuarios.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Usuarios", color = MaterialTheme.colorScheme.onPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AzulPrimary)
            )
        },
        containerColor = PanelBg
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            if (lista.isEmpty()) {
                Text("No hay usuarios registrados.")
            } else {
                Card(
                    colors = CardDefaults.cardColors(containerColor = PanelBg),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn(Modifier.fillMaxSize().padding(vertical = 6.dp)) {
                        items(lista) { u ->
                            ListItem(
                                headlineContent = { Text(u.nombre) },
                                supportingContent = { Text("${u.email} • Rol: ${u.rol}") },
                                trailingContent = {
                                    AssistChip(
                                        onClick = { /* ver/editar */ onEditarUsuario(u) },
                                        label = { Text(if (u.activo) "Activo" else "Inactivo") }
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onEditarUsuario(u) }
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                            Divider(color = Color.Black.copy(alpha = 0.06f))
                        }
                    }
                }
            }
        }
    }
}
