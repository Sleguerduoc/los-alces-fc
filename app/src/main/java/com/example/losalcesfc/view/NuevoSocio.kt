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
import android.location.Geocoder
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoSocioScreen(
    onBack: () -> Unit,
    onSaved: (Socio) -> Unit = {}
) {
    val socioVM: SocioViewModel = viewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    // --------- Estados del formulario ---------
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
    // Domicilio
    var domicilio by remember { mutableStateOf("") }
    var errDomicilio by remember { mutableStateOf<String?>(null) }

    // Coordenadas asociadas al domicilio
    var latitud by remember { mutableStateOf<Double?>(null) }
    var longitud by remember { mutableStateOf<Double?>(null) }

    // Mapa
    var googleMap by remember { mutableStateOf<GoogleMap?>(null) }
    var mapView by remember { mutableStateOf<MapView?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            mapView?.onPause()
            mapView?.onDestroy()
        }
    }



    fun validar(): Boolean {
        errNombre = if (nombre.isBlank()) "Nombre requerido" else null
        errRut = if (!esRutValido(rut)) "RUT inválido (formato 12.345.678-5)" else null
        errEmail = if (!emailRegex.matches(email)) "Email inválido" else null
        return errNombre == null && errRut == null && errEmail == null
    }

    // --------- Cámara ---------
    var fotoPath by remember { mutableStateOf<String?>(null) }
    var pendingFile by remember { mutableStateOf<File?>(null) }
    var pendingUri by remember { mutableStateOf<Uri?>(null) }

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
            val (file, uri) = createImageOutput(context)
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
            val (file, uri) = createImageOutput(context)
            pendingFile = file
            pendingUri = uri
            takePictureLauncher.launch(uri)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar nuevo socio", color = MaterialTheme.colorScheme.onPrimary) },
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
            // Foto (preview)
            if (fotoPath != null) {
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
                Text(if (fotoPath == null) "Tomar foto" else "Repetir foto")
            }

            // --- Campos ---
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

            // --- Domicilio + mapa ---
            OutlinedTextField(
                value = domicilio,
                onValueChange = {
                    domicilio = it
                    errDomicilio = null
                },
                label = { Text("Domicilio (calle, número, comuna)") },
                isError = errDomicilio != null,
                supportingText = {
                    if (errDomicilio != null) {
                        Text(errDomicilio!!)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (domicilio.isBlank()) {
                        errDomicilio = "Ingresa un domicilio para buscar"
                        return@Button
                    }

                    scope.launch {
                        try {
                            val geocoder = Geocoder(context, Locale.getDefault())

                            // Geocodificación en hilo de IO
                            val results = withContext(Dispatchers.IO) {
                                geocoder.getFromLocationName(domicilio, 1)
                            }

                            if (!results.isNullOrEmpty()) {
                                val loc = results[0]
                                val latLng = LatLng(loc.latitude, loc.longitude)
                                latitud = loc.latitude
                                longitud = loc.longitude

                                googleMap?.let { map ->
                                    map.clear()
                                    map.addMarker(
                                        MarkerOptions()
                                            .position(latLng)
                                            .title("Domicilio socio")
                                    )
                                    map.animateCamera(
                                        CameraUpdateFactory.newLatLngZoom(latLng, 16f)
                                    )
                                }
                            } else {
                                snackbar.showSnackbar("No se encontró la dirección")
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                            snackbar.showSnackbar("Error al buscar la dirección")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver domicilio en el mapa")
            }

            AndroidView(
                factory = { ctx ->
                    MapView(ctx).apply {
                        mapView = this
                        onCreate(null)

                        getMapAsync { map ->
                            googleMap = map

                            // Posición inicial (Chile)
                            val chile = LatLng(-33.45, -70.6667)
                            map.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(chile, 5f)
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE0E0E0)) // solo para que se vea el borde del "cuadrado"
            ) { mv ->
                // Esto se ejecuta en recomposiciones
                mv.onResume()
            }


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

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Activo", color = TextoAzul)
                Switch(checked = activo, onCheckedChange = { activo = it })
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) {
                    Text("Cancelar")
                }
                Button(
                    onClick = {
                        if (validar()) {
                            val socio = Socio(
                                nombre = nombre.trim(),
                                rut = rut.trim(),
                                email = email.trim(),
                                telefono = telefono.trim().ifBlank { null },
                                plan = plan,
                                activo = activo,
                                fotoPath = fotoPath,
                                domicilio = domicilio.trim().ifBlank { null },
                                latitud = latitud,
                                longitud = longitud
                            )
                            socioVM.guardar(socio)
                            vibrarCorto(context); sonidoConfirmacion()
                            scope.launch { snackbar.showSnackbar("Socio registrado: ${socio.nombre}") }
                            onSaved(socio)
                        } else {
                            vibrarError(context); sonidoError()
                            scope.launch { snackbar.showSnackbar("Revisa los campos marcados") }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DoradoAccent,
                        contentColor = Color(0xFF1B1B1B)
                    ),
                    modifier = Modifier.weight(1f)
                ) { Text("Guardar") }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}


val emailRegex =
    Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")

fun esRutValido(input: String): Boolean {
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

private fun createImageOutput(context: android.content.Context): Pair<File, Uri> {
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
