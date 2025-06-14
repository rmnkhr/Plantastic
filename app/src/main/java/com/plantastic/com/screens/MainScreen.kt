package com.plantastic.com.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.plantastic.com.navigation.BottomNavItem
import com.plantastic.com.navigation.BottomNavigationBar

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(BottomNavItem.Home.route) { HomeScreen() }
            composable(BottomNavItem.Garden.route) { GardenScreen() }
            composable(BottomNavItem.Notifications.route) { NotificationsScreen(navController) }
            composable(BottomNavItem.Profile.route) { ProfileScreen(navController) } // Pass NavController
            composable(com.plantastic.com.Destinations.ADD_NOTIFICATION) { AddNotificationScreen(navController) }

            // New Profile sub-screens
            composable(com.plantastic.com.Destinations.PRIVACY_POLICY) { PrivacyPolicyScreen() }
            composable(com.plantastic.com.Destinations.TERMS_AND_CONDITIONS) { TermsAndConditionsScreen() }
            composable(com.plantastic.com.Destinations.LICENSES) { LicensesScreen() }
            composable(com.plantastic.com.Destinations.RATE_THE_APP) { RateTheAppScreen() }
            composable(com.plantastic.com.Destinations.STATISTICS) { StatisticsScreen() }
            composable(com.plantastic.com.Destinations.EDIT_PROFILE) { EditProfileScreen() }
            // Onboarding is typically handled by the main app NavHost, but if a profile item needs to re-trigger it:
            composable(com.plantastic.com.Destinations.ONBOARDING) { OnboardingScreen(navController) } // Assuming OnboardingScreen can take a NavController from this graph too
        }
    }
}