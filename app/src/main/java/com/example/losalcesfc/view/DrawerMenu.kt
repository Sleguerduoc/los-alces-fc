package com.example.losalcesfc.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.losalcesfc.ui.theme.AzulPrimary
import com.example.losalcesfc.ui.theme.DoradoAccent
import com.example.losalcesfc.ui.theme.PanelBg
import kotlinx.coroutines.launch

// ------- Navigation Compose (nav interno del Drawer) -------
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

// Pantallas internas del Drawer (en este mismo package)
import com.example.losalcesfc.view.NuevoSocioScreen
import com.example.losalcesfc.view.ListaSociosScreen
import com.example.losalcesfc.view.EditarSocioScreen

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
    const val EDITAR_SOCIO = "editar_socio/{id}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerMenu(
    userName: String,
    userEmail: String? = null,

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
                            // Navegación interna del Drawer
                            when (item.id) {
                                DrawerRoutes.INICIO      -> innerNav.navigate(DrawerRoutes.INICIO)      { launchSingleTop = true }
                                DrawerRoutes.SOCIOS_MENU -> innerNav.navigate(DrawerRoutes.SOCIOS_MENU) { launchSingleTop = true }
                                "equipos"   -> {  }
                                "finanzas"  -> { }
                                "ajustes"   -> { }
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
                DrawerNav(nav = innerNav)
            }
        }
    }
}


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
                    // tras guardar, ir directo a la lista:
                    nav.navigate(DrawerRoutes.LISTA_SOCIOS) {
                        launchSingleTop = true
                        popUpTo(DrawerRoutes.SOCIOS_MENU) { inclusive = false }
                    }
                }
            )
        }

        // EDITAR SOCIO
        composable(route = DrawerRoutes.EDITAR_SOCIO) { backStackEntry ->
            val socioId = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            EditarSocioScreen(
                socioId = socioId,
                onBack = { nav.popBackStack() }
            )
        }

        // LISTA DE SOCIOS
        composable(DrawerRoutes.LISTA_SOCIOS) {
            ListaSociosScreen(
                onBack = { nav.popBackStack() },
                onNuevoSocio = { nav.navigate(DrawerRoutes.NUEVO_SOCIO) },
                onEditarSocio = { socio ->
                    nav.navigate("editar_socio/${socio.id}")
                }
            )
        }
    }
}



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

        // Lista de opciones
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

