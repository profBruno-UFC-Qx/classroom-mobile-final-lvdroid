package com.levi.qxdapp.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Assignment
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
import com.levi.qxdapp.presentation.client.order.OrdersView
import androidx.navigation.NavType
import androidx.navigation.navArgument


private val BluePrimary = Color(0xFF1964C3)
private val GrayUnselected = Color(0xFF8A8F9E)


sealed class BottomNavRoute(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavRoute("nav_home", "Mapa", Icons.Outlined.Map)
    object Orders : BottomNavRoute("nav_orders", "Pedidos", Icons.Outlined.Assignment)
    object Profile : BottomNavRoute("nav_profile", "Perfil", Icons.Outlined.Person)
}

val bottomNavItems = listOf(
    BottomNavRoute.Home,
    BottomNavRoute.Orders,
    BottomNavRoute.Profile
)


@Composable
fun MainScreen(onLogoutClick: () -> Unit = {}) {
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
                HomeView(
                    onProfileClick = {
                        navController.navigate(BottomNavRoute.Profile.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onSearchClick = { filter -> 
                        navController.navigate("search_products/$filter")
                    }
                )
            }
            composable(BottomNavRoute.Orders.route) {
                OrdersView()
            }
            composable(BottomNavRoute.Profile.route) {
                com.levi.qxdapp.presentation.client.profile.ClientProfileScreen(
                    onLogoutClick = onLogoutClick
                )
            }
            composable("search_products") {
                com.levi.qxdapp.presentation.client.products.ProductsScreen(
                    initialFilter = "Todos",
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(
                route = "search_products/{filter}",
                arguments = listOf(navArgument("filter") { type = NavType.StringType })
            ) { backStackEntry ->
                val filter = backStackEntry.arguments?.getString("filter") ?: "Todos"
                com.levi.qxdapp.presentation.client.products.ProductsScreen(
                    initialFilter = filter,
                    onBackClick = { navController.popBackStack() }
                )
            }

        }
    }
}


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
