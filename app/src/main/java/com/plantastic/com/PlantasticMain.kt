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

    // Profile Screen items
    const val PRIVACY_POLICY = "privacy_policy"
    const val TERMS_AND_CONDITIONS = "terms_and_conditions"
    const val LICENSES = "licenses"
    const val RATE_THE_APP = "rate_the_app"
    const val STATISTICS = "statistics"
    const val EDIT_PROFILE = "edit_profile"
    // const val PROFILE = "profile" // Not needed here as ProfileScreen is part of MainScreen's NavGraph
    const val SEARCH_PLANTS = "search_plants"
    const val PLANT_DETAIL = "plant_detail/{plantId}"
}