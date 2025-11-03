package com.example.losalcesfc.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.losalcesfc.ui.theme.AzulPrimary
import com.example.losalcesfc.ui.theme.DoradoAccent
import com.example.losalcesfc.ui.theme.PanelBg
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.lazy.items



// ------- Navigation Compose (nav interno del Drawer) -------
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

// 👇 si tu NuevoSocioScreen está en otro package, ajusta este import:
import com.example.losalcesfc.view.NuevoSocioScreen

data class DrawerItem(
    val label: String,
    val icon: ImageVector,
    val id: String
)

/** Rutas internas del Drawer */
object DrawerRoutes {
    const val INICIO = "inicio"
    const val SOCIOS_MENU = "socios_menu"
    const val NUEVO_SOCIO = "nuevo_socio"
    const val LISTA_SOCIOS = "lista_socios"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerMenu(
    userName: String,
    userEmail: String? = null,
    // 'current' ya no se usa para seleccionar visualmente (el contenido ahora lo maneja el nav interno),
    // pero lo mantenemos para no romper llamadas existentes.
    current: String = DrawerRoutes.INICIO,
    onLogout: () -> Unit,
) {
    val items = listOf(
        DrawerItem("Inicio",  Icons.Filled.Home,         DrawerRoutes.INICIO),
        DrawerItem("Socios",  Icons.Filled.Group,        DrawerRoutes.SOCIOS_MENU),
        DrawerItem("Equipos", Icons.Filled.SportsSoccer, "equipos"),   // pendiente
        DrawerItem("Finanzas",Icons.Filled.TrendingUp,   "finanzas"),  // pendiente
        DrawerItem("Ajustes", Icons.Filled.Settings,     "ajustes"),   // pendiente
    )

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // 👇 controlador de navegación interno (contenido principal del Drawer)
    val innerNav = rememberNavController()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = false, // solo botón hamburguesa
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = PanelBg) {

                // Header
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                ) {
                    Text(
                        text = "Los Alces F.C.",
                        style = MaterialTheme.typography.titleLarge,
                        color = AzulPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(text = userName, style = MaterialTheme.typography.bodyMedium)
                    userEmail?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                Divider()

                // Items
                items.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(item.label) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            // 👉 navegación interna del Drawer
                            when (item.id) {
                                DrawerRoutes.INICIO      -> innerNav.navigate(DrawerRoutes.INICIO)      { launchSingleTop = true }
                                DrawerRoutes.SOCIOS_MENU -> innerNav.navigate(DrawerRoutes.SOCIOS_MENU) { launchSingleTop = true }
                                "equipos"   -> { /* TODO: agrega ruta cuando tengas pantalla */ }
                                "finanzas"  -> { /* TODO */ }
                                "ajustes"   -> { /* TODO */ }
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = null) },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = DoradoAccent.copy(alpha = 0.25f),
                            selectedIconColor = AzulPrimary,
                            selectedTextColor = AzulPrimary,
                        ),
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }

                Spacer(Modifier.weight(1f))

                // Logout
                NavigationDrawerItem(
                    label = { Text("Cerrar sesión") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onLogout()
                    },
                    icon = { Icon(Icons.Filled.Logout, contentDescription = null) },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = DoradoAccent.copy(alpha = 0.25f),
                    ),
                    modifier = Modifier
                        .padding(NavigationDrawerItemDefaults.ItemPadding)
                        .padding(bottom = 12.dp)
                )
            }
        }
    ) {
        // Scaffold con botón hamburguesa para abrir el drawer
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "SIGA – Los Alces F.C.",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Abrir menú",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = AzulPrimary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            containerColor = PanelBg
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.TopCenter
            ) {
                // 👇 Aquí vive la navegación interna del Drawer
                DrawerNav(nav = innerNav)
            }
        }
    }
}

/* ===================================================================== */
/* ===================  NAV INTERNO DEL DRAWER  ======================== */
/* ===================================================================== */

@Composable
private fun DrawerNav(nav: NavHostController) {
    NavHost(navController = nav, startDestination = DrawerRoutes.INICIO) {

        // INICIO (contenido principal)
        composable(DrawerRoutes.INICIO) {
            InicioPanel()
        }

        // MENÚ DE SOCIOS
        composable(DrawerRoutes.SOCIOS_MENU) {
            SociosMenuScreen(
                onNuevoSocio = { nav.navigate(DrawerRoutes.NUEVO_SOCIO) },
                onVerSocios  = { nav.navigate(DrawerRoutes.LISTA_SOCIOS) }
            )
        }

        // NUEVO SOCIO (pantalla en /view)
        composable(DrawerRoutes.NUEVO_SOCIO) {
            NuevoSocioScreen(
                onBack = { nav.popBackStack() },
                onSaved = {
                    // tras guardar, vuelve al menú de socios (o al inicio, como prefieras)
                    nav.popBackStack()
                }
            )
        }

        // LISTA DE SOCIOS (placeholder simple)
        composable(DrawerRoutes.LISTA_SOCIOS) {
            ListaSociosPlaceholder(
                onVolver = { nav.popBackStack() }
            )
        }
    }
}

/* ====================  Composables de ejemplo  ======================= */

@Composable
private fun InicioPanel() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Panel principal", style = MaterialTheme.typography.titleLarge, color = AzulPrimary, fontWeight = FontWeight.SemiBold)
        Text("• Socios activos: 124")
        Text("• Equipos registrados: 4")
        Text("• Próximo partido: Domingo 18:00")
    }
}

@Composable
private fun SociosMenuScreen(
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

        // Lista de opciones (botones tipo ítem)
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

@Composable
fun ListaSociosPlaceholder(
    onVolver: () -> Unit
) {
    // Datos demo
    val sociosDemo = remember {
        listOf(
            SocioDemo("Juan Pérez",     "12.345.678-5", "Básico",  true),
            SocioDemo("Ana Gómez",      "16.789.123-2", "Oro",     true),
            SocioDemo("Luis Alvarado",  "9.876.543-1",  "Plata",   false),
            SocioDemo("María Torres",   "22.334.556-7", "Premium", true),
            SocioDemo("Carlos Díaz",    "18.222.444-6", "Básico",  false),
        )
    }

    var query by remember { mutableStateOf("") }
    val filtrados = remember(query, sociosDemo) {
        if (query.isBlank()) sociosDemo
        else sociosDemo.filter {
            it.nombre.contains(query, ignoreCase = true) ||
                    it.rut.contains(query, ignoreCase = true) ||
                    it.plan.contains(query, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Listado de Socios",
            style = MaterialTheme.typography.titleLarge,
            color = AzulPrimary,
            fontWeight = FontWeight.SemiBold
        )

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Buscar por nombre, RUT o plan") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Card(
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(containerColor = PanelBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 6.dp)
            ) {
                items(filtrados) { socio ->
                    ListItem(
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = null,
                                tint = AzulPrimary
                            )
                        },
                        headlineContent = {
                            Text(socio.nombre, fontWeight = FontWeight.SemiBold)
                        },
                        supportingContent = {
                            Text("${socio.rut} • Plan: ${socio.plan}")
                        },
                        trailingContent = {
                            val chipColor = if (socio.activo) Color(0xFF2E7D32) else Color(0xFF9E9E9E)
                            AssistChip(
                                onClick = { /* noop demo */ },
                                label = { Text(if (socio.activo) "Activo" else "Inactivo") },
                                colors = AssistChipDefaults.assistChipColors(
                                    labelColor = Color.White,
                                    containerColor = chipColor
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { /* abrir detalle, futuro */ }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    )
                    Divider(color = Color.Black.copy(alpha = 0.06f))
                }

                if (filtrados.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No se encontraron socios para “$query”.")
                        }
                    }
                }

                item { Spacer(Modifier.height(8.dp)) }
                item {
                    TextButton(onClick = onVolver, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Text("Volver")
                    }
                }
                item { Spacer(Modifier.height(8.dp)) }
            }
        }
    }
}

private data class SocioDemo(
    val nombre: String,
    val rut: String,
    val plan: String,
    val activo: Boolean
)

