package com.plantastic.com.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.*
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
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(BottomNavItem.Home.route) { HomeScreen(navController) } // Pass NavController
            composable(BottomNavItem.Garden.route) { GardenScreen() }
            composable(BottomNavItem.Notifications.route) { NotificationsScreen(navController) }
            composable(BottomNavItem.Profile.route) { ProfileScreen() }
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
        }
    }
}