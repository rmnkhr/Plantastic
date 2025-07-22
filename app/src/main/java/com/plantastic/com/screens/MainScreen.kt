package com.plantastic.com.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.plantastic.com.Destinations
import com.plantastic.com.data.PlantData
import com.plantastic.com.data.UserPlantRepository
import com.plantastic.com.navigation.BottomNavItem
import com.plantastic.com.navigation.BottomNavigationBar
import com.plantastic.com.utils.AssetLoader
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val userPlantRepository = remember { UserPlantRepository(context) } // Instantiate repository
    var allPlants by remember { mutableStateOf<List<PlantData>>(emptyList()) }

    // Load plant data
    LaunchedEffect(Unit) {
        launch { // Use launch for coroutine
            allPlants = AssetLoader().loadPlantsFromAssets(context)
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .background(Color.White) // якщо треба
        ) {
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Home.route,
                modifier = Modifier
                    .padding(paddingValues)
            ) {
                composable(BottomNavItem.Home.route) { HomeScreen(navController) } // Pass NavController
                composable(BottomNavItem.Garden.route) { GardenScreen() }
                composable(BottomNavItem.Notifications.route) { NotificationsScreen(navController) }
                composable(BottomNavItem.Profile.route) { ProfileScreen(navController) }
                composable(Destinations.ADD_NOTIFICATION) { AddNotificationScreen(navController) }
                composable(Destinations.SEARCH_PLANTS) {
                    SearchScreen(
                        allPlants = allPlants,
                        userPlantRepository = userPlantRepository, // Pass repository
                        onPlantSelected = { plant ->
                            // This will be handled within SearchScreen now
                            println("Plant selected: ${plant.name} - will be added via repository")
                        }
                    )
                }
                composable(BottomNavItem.Notifications.route) { NotificationsScreen(navController) }
                composable(BottomNavItem.Profile.route) { ProfileScreen(navController) } // Pass NavController
                composable(Destinations.ADD_NOTIFICATION) { AddNotificationScreen(navController) }

                // New Profile sub-screens
                composable(Destinations.PRIVACY_POLICY) { PrivacyPolicyScreen() }
                composable(Destinations.TERMS_AND_CONDITIONS) { TermsAndConditionsScreen() }
                composable(Destinations.LICENSES) { LicensesScreen() }
                composable(Destinations.RATE_THE_APP) { RateTheAppScreen() }
                composable(Destinations.STATISTICS) { StatisticsScreen() }
                composable(Destinations.EDIT_PROFILE) { EditProfileScreen() }
                // Onboarding is typically handled by the main app NavHost, but if a profile item needs to re-trigger it:
                composable(Destinations.ONBOARDING) { OnboardingScreen(navController) } // Assuming OnboardingScreen can take a NavController from this graph too
            }
        }
    }
}