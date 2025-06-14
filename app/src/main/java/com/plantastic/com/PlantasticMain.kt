package com.plantastic.com

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.plantastic.com.Destinations.MAIN
import com.plantastic.com.Destinations.ONBOARDING
import com.plantastic.com.screens.MainScreen
import com.plantastic.com.screens.OnboardingScreen
import com.plantastic.com.screens.wasOnboardingShown

@Composable
fun PlantasticApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val startDestination = if (wasOnboardingShown(context)) MAIN else ONBOARDING

    NavHost(navController = navController, startDestination = startDestination) {
        composable(ONBOARDING) { OnboardingScreen(navController) }
        composable(MAIN) { MainScreen() }
    }
}

object Destinations {
    const val ONBOARDING = "onboarding"
    const val MAIN = "main"
    const val ADD_NOTIFICATION = "add_notification" // New destination
}