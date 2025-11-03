package com.example.losalcesfc.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.losalcesfc.ui.theme.AzulPrimary
import com.example.losalcesfc.ui.theme.DoradoAccent
import com.example.losalcesfc.ui.theme.PanelBg
import com.example.losalcesfc.ui.theme.TextoAzul
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoSocioScreen(
    onBack: () -> Unit,
    onSaved: (NuevoSocioData) -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    var nombre by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }

    val planes = listOf("Básico", "Plata", "Oro", "Premium")
    var planExpanded by remember { mutableStateOf(false) }
    var plan by remember { mutableStateOf(planes.first()) }

    var activo by remember { mutableStateOf(true) }

    // Errores
    var errNombre by remember { mutableStateOf<String?>(null) }
    var errRut by remember { mutableStateOf<String?>(null) }
    var errEmail by remember { mutableStateOf<String?>(null) }

    fun validar(): Boolean {
        errNombre = if (nombre.isBlank()) "Nombre requerido" else null
        errRut = if (!esRutValido(rut)) "RUT inválido (formato 12.345.678-5)" else null
        errEmail = if (!emailRegex.matches(email)) "Email inválido" else null
        return errNombre == null && errRut == null && errEmail == null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Registrar nuevo socio",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AzulPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbar) },
        containerColor = PanelBg
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(PanelBg)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it; errNombre = null },
                label = { Text("Nombre completo") },
                isError = errNombre != null,
                supportingText = { if (errNombre != null) Text(errNombre!!) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )

            // RUT
            OutlinedTextField(
                value = rut,
                onValueChange = { rut = it; errRut = null },
                label = { Text("RUT (12.345.678-5)") },
                isError = errRut != null,
                supportingText = { if (errRut != null) Text(errRut!!) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; errEmail = null },
                label = { Text("Email") },
                isError = errEmail != null,
                supportingText = { if (errEmail != null) Text(errEmail!!) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Teléfono (opcional)
            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono (opcional)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Phone
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Plan
            ExposedDropdownMenuBox(
                expanded = planExpanded,
                onExpandedChange = { planExpanded = it }
            ) {
                OutlinedTextField(
                    value = plan,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Plan de socio") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = planExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = planExpanded,
                    onDismissRequest = { planExpanded = false }
                ) {
                    planes.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = { plan = it; planExpanded = false }
                        )
                    }
                }
            }

            // Activo
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Activo", color = TextoAzul)
                Switch(checked = activo, onCheckedChange = { activo = it })
            }

            Spacer(Modifier.height(8.dp))

            // Botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }
                Button(
                    onClick = {
                        if (validar()) {
                            val data = NuevoSocioData(
                                nombre = nombre.trim(),
                                rut = rut.trim(),
                                email = email.trim(),
                                telefono = telefono.trim().ifBlank { null },
                                plan = plan,
                                activo = activo
                            )
                            // DEMO: snackbar + callback
                            scope.launch {
                                snackbar.showSnackbar("Socio registrado (demo): ${data.nombre}")
                            }
                            onSaved(data)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DoradoAccent,
                        contentColor = Color(0xFF1B1B1B)
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Guardar")
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

/* ----------------------- Utilidades & modelo ----------------------- */

private val emailRegex =
    Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")

/**
 * Valida RUT chileno: 12.345.678-5 o 12345678-5
 */
private fun esRutValido(input: String): Boolean {
    val cleaned = input.replace(".", "").replace("-", "").uppercase()
    if (cleaned.length < 2) return false
    val dv = cleaned.last()
    val num = cleaned.dropLast(1)
    if (num.any { !it.isDigit() }) return false

    var s = 1
    num.reversed().forEachIndexed { i, ch ->
        s = (s + (ch.code - 48) * (9 - (i % 6))) % 11
    }
    val dvCalc = if (s == 0) 'K' else (s + 47).toChar()
    return dv == dvCalc
}

data class NuevoSocioData(
    val nombre: String,
    val rut: String,
    val email: String,
    val telefono: String?,
    val plan: String,
    val activo: Boolean
)
