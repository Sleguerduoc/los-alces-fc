package com.example.losalcesfc.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

// Navigation Compose
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.example.losalcesfc.ui.theme.AzulPrimary
import com.example.losalcesfc.ui.theme.DoradoAccent
import com.example.losalcesfc.ui.theme.PanelBg

// IMPORTA tus pantallas reales:
import com.example.losalcesfc.view.NuevoSocioScreen
import com.example.losalcesfc.view.ListaSociosScreen
import com.example.losalcesfc.view.AjustesScreen
import com.example.losalcesfc.view.UsuariosMenuScreen
import com.example.losalcesfc.view.NuevoUsuarioScreen
import com.example.losalcesfc.view.ListaUsuariosScreen

data class DrawerItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

object DrawerRoutes {
    const val INICIO = "inicio"

    // Socios
    const val SOCIOS_MENU = "socios_menu"
    const val NUEVO_SOCIO = "nuevo_socio"
    const val LISTA_SOCIOS = "lista_socios"

    // Ajustes / Usuarios
    const val AJUSTES = "ajustes"
    const val USUARIOS_MENU = "usuarios_menu"
    const val NUEVO_USUARIO = "nuevo_usuario"
    const val LISTA_USUARIOS = "lista_usuarios"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerMenu(
    userName: String,
    userEmail: String? = null,
    onLogout: () -> Unit,
) {
    // Ítems del Drawer (lado izquierdo)
    val items = listOf(
        DrawerItem("Inicio",  Icons.Filled.Home,         DrawerRoutes.INICIO),
        DrawerItem("Socios",  Icons.Filled.Group,        DrawerRoutes.SOCIOS_MENU),
        DrawerItem("Ajustes", Icons.Filled.Settings,     DrawerRoutes.AJUSTES),
        // Puedes agregar después: Equipos, Finanzas, etc.
    )

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Nav interno para el contenido central del Drawer
    val innerNav = rememberNavController()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = false, // solo botón hamburguesa
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = PanelBg) {

                // Header simple
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

                // Lista de items
                items.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(item.label) },
                        selected = false, // si quieres marcar seleccionado, usa innerNav.currentBackStackEntry
                        onClick = {
                            scope.launch { drawerState.close() }
                            innerNav.navigate(item.route) { launchSingleTop = true }
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
        // Scaffold con botón hamburguesa
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

/* ===================  NAV INTERNO (contenido central)  =================== */

@Composable
private fun DrawerNav(nav: NavHostController) {
    NavHost(navController = nav, startDestination = DrawerRoutes.INICIO) {

        /* --------- INICIO --------- */
        composable(DrawerRoutes.INICIO) {
            InicioPanel()
        }

        /* --------- SOCIOS --------- */
        composable(DrawerRoutes.SOCIOS_MENU) {
            SociosMenuScreen(
                onNuevoSocio = { nav.navigate(DrawerRoutes.NUEVO_SOCIO) },
                onVerSocios  = { nav.navigate(DrawerRoutes.LISTA_SOCIOS) }
            )
        }
        composable(DrawerRoutes.NUEVO_SOCIO) {
            NuevoSocioScreen(
                onBack = { nav.popBackStack() },
                onSaved = { nav.popBackStack() } // vuelve al menú de socios, ajusta si quieres
            )
        }
        composable(DrawerRoutes.LISTA_SOCIOS) {
            ListaSociosScreen(
                onBack = { nav.popBackStack() },
                onNuevoSocio = { nav.navigate(DrawerRoutes.NUEVO_SOCIO) },
                onEditarSocio = { socio ->
                    // si ya tienes ruta editar con ID, agrégala aquí
                    // nav.navigate("editar_socio/${socio.id}")
                }
            )
        }

        /* --------- AJUSTES / USUARIOS --------- */
        composable(DrawerRoutes.AJUSTES) {
            AjustesScreen(
                onGestionUsuarios = { nav.navigate(DrawerRoutes.USUARIOS_MENU) }
            )
        }
        composable(DrawerRoutes.USUARIOS_MENU) {
            UsuariosMenuScreen(
                onNuevoUsuario = { nav.navigate(DrawerRoutes.NUEVO_USUARIO) },
                onVerUsuarios  = { nav.navigate(DrawerRoutes.LISTA_USUARIOS) }
            )
        }
        composable(DrawerRoutes.NUEVO_USUARIO) {
            NuevoUsuarioScreen(
                onBack = { nav.popBackStack() },
                onSaved = { nav.popBackStack() }
            )
        }
        composable(DrawerRoutes.LISTA_USUARIOS) {
            ListaUsuariosScreen(
                onBack = { nav.popBackStack() },
                onEditarUsuario = { /* TODO: navegar a editar */ }
            )
        }
    }
}

/* ====================  Contenido de INICIO (simple)  ===================== */

@Composable
private fun InicioPanel() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "Panel principal",
            style = MaterialTheme.typography.titleLarge,
            color = AzulPrimary,
            fontWeight = FontWeight.SemiBold
        )
        Text("• Socios activos: 124")
        Text("• Equipos registrados: 4")
        Text("• Próximo partido: Domingo 18:00")
    }
}
