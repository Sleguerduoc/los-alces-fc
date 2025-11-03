package com.example.losalcesfc.view

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.losalcesfc.data.model.Socio
import com.example.losalcesfc.ui.theme.AzulPrimary
import com.example.losalcesfc.ui.theme.DoradoAccent
import com.example.losalcesfc.ui.theme.PanelBg
import com.example.losalcesfc.utils.decodeScaledBitmap
import com.example.losalcesfc.viewmodel.SocioViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaSociosScreen(
    onBack: () -> Unit,
    onNuevoSocio: () -> Unit,
    onEditarSocio: (Socio) -> Unit,
    socioVM: SocioViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val socios by socioVM.socios.collectAsState()
    var query by remember { mutableStateOf("") }
    val snackbar = remember { SnackbarHostState() }

    val filtrados = remember(query, socios) {
        if (query.isBlank()) socios
        else socios.filter {
            it.nombre.contains(query, ignoreCase = true) ||
                    it.rut.contains(query, ignoreCase = true) ||
                    it.plan.contains(query, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Listado de socios", color = MaterialTheme.colorScheme.onPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                actions = {
                    // Crear nuevo
                    TextButton(onClick = onNuevoSocio) {
                        Icon(Icons.Filled.PersonAdd, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                        Spacer(Modifier.width(6.dp))
                        Text("Nuevo", color = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AzulPrimary)
            )
        },
        snackbarHost = { SnackbarHost(snackbar) },
        containerColor = PanelBg
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Buscar por nombre, RUT o plan") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            ElevatedCard(
                modifier = Modifier.fillMaxSize(),
                colors = CardDefaults.elevatedCardColors(containerColor = PanelBg)
            ) {
                if (filtrados.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay resultados para “$query”.")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 6.dp)
                    ) {
                        items(filtrados, key = { it.id }) { socio ->
                            SocioRow(
                                socio = socio,
                                onClick = { onEditarSocio(socio) },
                                onEditar = { onEditarSocio(socio) },
                                onEliminar = {
                                    scope.launch {
                                        runCatching { socioVM.eliminar(socio) }
                                            .onSuccess { snackbar.showSnackbar("Socio eliminado: ${socio.nombre}") }
                                            .onFailure { snackbar.showSnackbar("Error al eliminar: ${it.message}") }
                                    }
                                }
                            )
                            Divider(color = Color.Black.copy(alpha = 0.06f))
                        }
                        item { Spacer(Modifier.height(8.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun SocioRow(
    socio: Socio,
    onClick: () -> Unit,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    ListItem(
        leadingContent = {
            SocioAvatar(fotoPath = socio.fotoPath)
        },
        headlineContent = {
            Text(socio.nombre, fontWeight = FontWeight.SemiBold)
        },
        supportingContent = {
            Text("${socio.rut} • Plan: ${socio.plan}")
        },
        trailingContent = {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                // Chip de estado
                AssistChip(
                    onClick = { /* noop */ },
                    label = { Text(if (socio.activo) "Activo" else "Inactivo") },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (socio.activo) Color(0xFF2E7D32) else Color(0xFF9E9E9E),
                        labelColor = Color.White
                    )
                )
                // Editar
                IconButton(onClick = onEditar) {
                    Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = AzulPrimary)
                }
                // Eliminar
                IconButton(onClick = onEliminar) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = Color(0xFFB00020))
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 6.dp)
    )
}

@Composable
private fun SocioAvatar(fotoPath: String?) {
    if (fotoPath.isNullOrBlank()) {
        Surface(
            tonalElevation = 2.dp,
            shape = MaterialTheme.shapes.small,
            color = Color.White,
            modifier = Modifier.size(48.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.Person, contentDescription = null, tint = AzulPrimary)
            }
        }
    } else {
        val bmp = remember(fotoPath) { fotoPath?.let { decodeScaledBitmap(it, 256) } }


        if (bmp != null) {
            Image(
                bitmap = bmp.asImageBitmap(),
                contentDescription = "Foto socio",
                modifier = Modifier
                    .size(48.dp)
                    .clip(MaterialTheme.shapes.small)
            )
        } else {

            Surface(
                tonalElevation = 2.dp,
                shape = MaterialTheme.shapes.small,
                color = Color.White,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.Person, contentDescription = null, tint = AzulPrimary)
                }
            }
        }
    }
}
