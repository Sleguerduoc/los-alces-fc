package com.example.losalcesfc.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.losalcesfc.data.model.Usuario
import com.example.losalcesfc.ui.theme.AzulPrimary
import com.example.losalcesfc.ui.theme.DoradoAccent
import com.example.losalcesfc.ui.theme.PanelBg
import com.example.losalcesfc.viewmodel.UsuarioViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoUsuarioScreen(
    onBack: () -> Unit,
    onSaved: (Usuario) -> Unit = {}
) {
    val usuarioVM: UsuarioViewModel = viewModel()
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf("Usuario") }
    var activo by remember { mutableStateOf(true) }

    // 👇 NUEVOS CAMPOS: contraseña
    var passwordInput by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val roles = listOf("Administrador", "Usuario")
    var rolExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar nuevo usuario", color = MaterialTheme.colorScheme.onPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = MaterialTheme.colorScheme.onPrimary)
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
                .background(PanelBg)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre completo") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = rut,
                onValueChange = { rut = it },
                label = { Text("RUT (12.345.678-5)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )

            // ---- Contraseña ----
            OutlinedTextField(
                value = passwordInput,
                onValueChange = {
                    passwordInput = it
                    passwordError = null
                },
                label = { Text("Contraseña") },
                singleLine = true,
                isError = passwordError != null,
                supportingText = {
                    if (passwordError != null) Text(passwordError!!)
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Password
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    passwordError = null
                },
                label = { Text("Confirmar contraseña") },
                singleLine = true,
                isError = passwordError != null,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // ---- Rol ----
            ExposedDropdownMenuBox(
                expanded = rolExpanded,
                onExpandedChange = { rolExpanded = it }
            ) {
                OutlinedTextField(
                    value = rol,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Rol") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = rolExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = rolExpanded,
                    onDismissRequest = { rolExpanded = false }
                ) {
                    roles.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                rol = it
                                rolExpanded = false
                            }
                        )
                    }
                }
            }

            // ---- Activo ----
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Activo")
                Switch(checked = activo, onCheckedChange = { activo = it })
            }

            Spacer(Modifier.height(8.dp))

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
                        // Validación de contraseña
                        if (passwordInput.isBlank() || passwordInput.length < 4) {
                            passwordError = "La contraseña debe tener al menos 4 caracteres"
                            return@Button
                        }
                        if (passwordInput != confirmPassword) {
                            passwordError = "Las contraseñas no coinciden"
                            return@Button
                        }

                        val nuevo = Usuario(
                            nombre = nombre.trim(),
                            email = email.trim(),
                            rut = rut.trim(),
                            rol = rol,
                            activo = activo,
                            fotoPath = null,
                            password = passwordInput  // 👈 TEXTO PLANO
                        )

                        scope.launch {
                            usuarioVM.guardar(nuevo)
                            snackbar.showSnackbar("Usuario registrado: ${nuevo.nombre}")
                        }

                        onSaved(nuevo)
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
        }
    }
}
