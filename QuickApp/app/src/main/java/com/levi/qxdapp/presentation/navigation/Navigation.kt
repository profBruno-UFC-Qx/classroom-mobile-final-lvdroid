package com.levi.qxdapp.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.levi.qxdapp.presentation.client.home.HomeView

// Cores do tema
val BluePrimary = Color(0xFF1964C3)
val GrayUnselected = Color(0xFF8A8F9E)

// Rotas do bottom navigation
sealed class BottomNavRoute(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavRoute("nav_home", "Início", Icons.Outlined.Home)
    object Map : BottomNavRoute("nav_map", "Mapa", Icons.Outlined.Map)
    object Orders : BottomNavRoute("nav_orders", "Pedidos", Icons.Outlined.Assignment)
    object Profile : BottomNavRoute("nav_profile", "Perfil", Icons.Outlined.Person)
}

val bottomNavItems = listOf(
    BottomNavRoute.Home,
    BottomNavRoute.Map,
    BottomNavRoute.Orders,
    BottomNavRoute.Profile
)


@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title
                            )
                        },
                        label = { Text(item.title) },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                // Evita empilhar múltiplas cópias da mesma tela
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BluePrimary,
                            selectedTextColor = BluePrimary,
                            unselectedIconColor = GrayUnselected,
                            unselectedTextColor = GrayUnselected,
                            indicatorColor = BluePrimary.copy(alpha = 0.1f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavRoute.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavRoute.Home.route) {
                HomeView()
            }
            composable(BottomNavRoute.Map.route) {
                PlaceholderScreen(title = "Mapa", subtitle = "Em breve: localização dos fornecedores")
            }
            composable(BottomNavRoute.Orders.route) {
                PlaceholderScreen(title = "Pedidos", subtitle = "Em breve: histórico de pedidos")
            }
            composable(BottomNavRoute.Profile.route) {
                PlaceholderScreen(title = "Perfil", subtitle = "Em breve: configurações da conta")
            }
        }
    }
}

/**
 * Tela temporária para abas ainda não implementadas.
 */
@Composable
fun PlaceholderScreen(title: String, subtitle: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$title\n$subtitle",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = GrayUnselected
        )
    }
}
