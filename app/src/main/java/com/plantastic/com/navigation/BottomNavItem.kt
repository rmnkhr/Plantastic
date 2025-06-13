package com.plantastic.com.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Home : BottomNavItem("home", "Home", Icons.Default.Home)
    object Garden : BottomNavItem("garden", "Garden", Icons.Default.Call)
    object Notifications : BottomNavItem("notifications", "Notifications", Icons.Default.Notifications)
    object Profile : BottomNavItem("profile", "Profile", Icons.Default.Person)

    companion object {
        val items = listOf(Home, Garden, Notifications, Profile)
    }
}