package com.levi.qxdapp.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

// Uilizando a cor principal do projeto
val BluePrimary = Color(0xFF1964C3)
val GrayUnselected = Color(0xFF8A8F9E)

data class BottomNavItem(
    val title: String,
    val icon: ImageVector
)
@Composable
fun BottomNavigationBar() {
    // Utilizado para controlar o estado do menu
}


