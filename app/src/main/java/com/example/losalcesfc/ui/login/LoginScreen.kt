package com.example.losalcesfc.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.losalcesfc.R
import com.example.losalcesfc.ui.theme.*
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import com.example.losalcesfc.utils.vibrarCorto
import com.example.losalcesfc.utils.vibrarError
import com.example.losalcesfc.utils.sonidoConfirmacion
import com.example.losalcesfc.utils.sonidoError

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit
) {
    val ui = viewModel.ui.collectAsState().value
    val focus = LocalFocusManager.current
    val ctx = LocalContext.current

    // ---- Estados para "Olvidaste tu contraseña" + snackbar ----
    var forgotOpen by remember { mutableStateOf(false) }
    var forgotEmail by remember { mutableStateOf("") }
    var forgotError by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun isValidEmail(e: String) =
        Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$").matches(e)

    LaunchedEffect(ui.authError) {
        if (ui.authError != null) {
            vibrarError(ctx)
            sonidoError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(AzulPrimary)
                .padding(padding),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(AppCornerRadius))
                        .background(PanelBg)
                        .padding(24.dp)
                ) {
                    // Logo
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_duoc_alces),
                            contentDescription = "Logo Los Alces F.C.",
                            modifier = Modifier.size(96.dp)
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "Bienvenido a SIGA – Los Alces F.C.",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextoAzul,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Inicia sesión para gestionar socios, equipos y finanzas",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoAzul.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(24.dp))

                    OutlinedTextField(
                        value = ui.emailOrRut,
                        onValueChange = { viewModel.onEmailChange(it) },
                        label = { Text("Email o RUT") },
                        singleLine = true,
                        isError = ui.emailError != null,
                        supportingText = {
                            if (ui.emailError != null) Text(ui.emailError!!)
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    var pwdVisible by remember { mutableStateOf(false) }
                    OutlinedTextField(
                        value = ui.password,
                        onValueChange = { viewModel.onPasswordChange(it) },
                        label = { Text("Contraseña") },
                        singleLine = true,
                        isError = ui.pwdError != null,
                        supportingText = {
                            if (ui.pwdError != null) Text(ui.pwdError!!)
                        },
                        visualTransformation = if (pwdVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val icon = if (pwdVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility
                            IconButton(onClick = { pwdVisible = !pwdVisible }) {
                                Icon(
                                    painter = painterResource(icon),
                                    contentDescription = null,
                                    tint = TextoAzul
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { focus.clearFocus() }),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (ui.authError != null) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = ui.authError,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    Button(
                        onClick = {
                            // feedback inmediato de acción
                            vibrarCorto(ctx)
                            sonidoConfirmacion()
                            viewModel.submit(onLoginSuccess)
                        },
                        enabled = !ui.isLoading,
                        shape = RoundedCornerShape(AppCornerRadius),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DoradoAccent,
                            contentColor = Color(0xFF1B1B1B)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        if (ui.isLoading) {
                            CircularProgressIndicator(
                                strokeWidth = 2.dp,
                                color = Color(0xFF1B1B1B),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Text("Ingresando…")
                        } else {
                            Text("Ingresar")
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    TextButton(
                        onClick = {
                            forgotEmail = ""
                            forgotError = null
                            forgotOpen = true
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("¿Olvidaste tu contraseña?", color = AzulPrimary)
                    }
                }
            }
        }
    }


    if (forgotOpen) {
        AlertDialog(
            onDismissRequest = { forgotOpen = false },
            title = { Text("Recuperar contraseña") },
            text = {
                Column {
                    OutlinedTextField(
                        value = forgotEmail,
                        onValueChange = {
                            forgotEmail = it
                            forgotError = null
                        },
                        singleLine = true,
                        label = { Text("Correo electrónico") },
                        isError = forgotError != null,
                        supportingText = {
                            if (forgotError != null) Text(forgotError!!)
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { focus.clearFocus() }),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (!isValidEmail(forgotEmail)) {
                        forgotError = "Formato de correo inválido"
                        // feedback de error
                        vibrarError(ctx)
                        sonidoError()
                        return@TextButton
                    }

                    viewModel.sendResetEmail(forgotEmail) { _, message ->
                        forgotOpen = false
                        scope.launch {
                            snackbarHostState.showSnackbar(message)
                        }
                        // pequeño feedback de confirmación de acción del diálogo
                        vibrarCorto(ctx)
                        sonidoConfirmacion()
                    }
                }) { Text("Enviar") }
            },
            dismissButton = {
                TextButton(onClick = { forgotOpen = false }) { Text("Cancelar") }
            }
        )
    }
}
