package com.example.losalcesfc.view

import android.net.Uri
import android.os.Environment
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.losalcesfc.data.model.Socio
import com.example.losalcesfc.ui.theme.AzulPrimary
import com.example.losalcesfc.ui.theme.DoradoAccent
import com.example.losalcesfc.ui.theme.PanelBg
import com.example.losalcesfc.ui.theme.TextoAzul
import com.example.losalcesfc.viewmodel.SocioViewModel
import kotlinx.coroutines.launch
import java.io.File
import com.example.losalcesfc.utils.CameraPermissionHelper
import com.example.losalcesfc.utils.decodeScaledBitmap
import com.example.losalcesfc.utils.vibrarCorto
import com.example.losalcesfc.utils.vibrarError
import com.example.losalcesfc.utils.sonidoConfirmacion
import com.example.losalcesfc.utils.sonidoError

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarSocioScreen(
    socioId: Int,
    onBack: () -> Unit,
    socioVM: SocioViewModel = viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    // Cargar socio
    var socio by remember { mutableStateOf<Socio?>(null) }
    LaunchedEffect(socioId) {
        socio = socioVM.obtenerPorId(socioId)
    }

    // Estados
    var nombre by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }

    val planes = listOf("Básico", "Plata", "Oro", "Premium")
    var planExpanded by remember { mutableStateOf(false) }
    var plan by remember { mutableStateOf(planes.first()) }
    var activo by remember { mutableStateOf(true) }

    // Foto
    var fotoPath by remember { mutableStateOf<String?>(null) }
    var pendingFile by remember { mutableStateOf<File?>(null) }
    var pendingUri by remember { mutableStateOf<Uri?>(null) }

    // Precarga cuando llega el socio
    LaunchedEffect(socio) {
        socio?.let {
            nombre = it.nombre
            rut = it.rut
            email = it.email
            telefono = it.telefono ?: ""
            plan = it.plan
            activo = it.activo
            fotoPath = it.fotoPath
        }
    }

    // Cámara
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && pendingFile != null) {
            fotoPath = pendingFile!!.absolutePath
        } else {
            pendingFile = null
            pendingUri = null
        }
    }

    val requestCameraPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val (file, uri) = createImageOutput2(context)
            pendingFile = file
            pendingUri = uri
            takePictureLauncher.launch(uri)
        } else {
            scope.launch { snackbar.showSnackbar("Permiso de cámara denegado") }
            vibrarError(context); sonidoError()
        }
    }

    fun openCamera() {
        val hasPermission = CameraPermissionHelper.ensureCameraPermission(
            context = context,
            requestPermissionLauncher = requestCameraPermission
        )
        if (hasPermission) {
            val (file, uri) = createImageOutput2(context)
            pendingFile = file
            pendingUri = uri
            takePictureLauncher.launch(uri)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar socio", color = MaterialTheme.colorScheme.onPrimary) },
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
        if (socio == null) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(PanelBg)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (!fotoPath.isNullOrBlank()) {
                val bmp = remember(fotoPath) { decodeScaledBitmap(fotoPath!!, 512) }
                if (bmp != null) {
                    Image(
                        bitmap = bmp.asImageBitmap(),
                        contentDescription = "Foto del socio",
                        modifier = Modifier
                            .size(160.dp)
                            .align(Alignment.CenterHorizontally)
                            .clip(RoundedCornerShape(10.dp))
                    )
                }
            }

            Button(
                onClick = { openCamera() },
                colors = ButtonDefaults.buttonColors(containerColor = DoradoAccent),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(Icons.Filled.CameraAlt, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(if (fotoPath.isNullOrBlank()) "Tomar foto" else "Reemplazar foto")
            }

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre completo") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
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

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                ),
                modifier = Modifier.fillMaxWidth()
            )

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

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Activo", color = TextoAzul)
                Switch(checked = activo, onCheckedChange = { activo = it })
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) {
                    Text("Cancelar")
                }
                Button(
                    onClick = {
                        if (nombre.isBlank()) {
                            vibrarError(context); sonidoError()
                            scope.launch { snackbar.showSnackbar("Nombre requerido") }
                            return@Button
                        }
                        if (!esRutValido(rut)) {
                            vibrarError(context); sonidoError()
                            scope.launch { snackbar.showSnackbar("RUT inválido") }
                            return@Button
                        }

                        val actualizado = socio!!.copy(
                            nombre = nombre.trim(),
                            rut = rut.trim(),
                            email = email.trim(),
                            telefono = telefono.trim().ifBlank { null },
                            plan = plan,
                            activo = activo,
                            fotoPath = fotoPath
                        )

                        scope.launch {
                            runCatching { socioVM.actualizar(actualizado) }
                                .onSuccess {
                                    vibrarCorto(context); sonidoConfirmacion()
                                    snackbar.showSnackbar("Socio actualizado")
                                    onBack()
                                }
                                .onFailure {
                                    vibrarError(context); sonidoError()
                                    snackbar.showSnackbar("Error: ${it.message}")
                                }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DoradoAccent),
                    modifier = Modifier.weight(1f)
                ) { Text("Guardar cambios") }
            }
        }
    }
}

/* --------- Helpers: salida de imagen --------- */

private fun createImageOutput2(context: android.content.Context): Pair<File, Uri> {
    val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        ?: throw IllegalStateException("No se pudo obtener la carpeta Pictures del app")
    if (!dir.exists()) dir.mkdirs()
    val file = File(dir, "socio_${System.currentTimeMillis()}.jpg")
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
    return file to uri
}
