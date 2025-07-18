package com.plantastic.com.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.plantastic.com.Destinations
import com.plantastic.com.data.PlantData
import com.plantastic.com.data.UserPlantRepository
import com.plantastic.com.screens.home.PlantCard
import com.plantastic.com.utils.AssetLoader
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val userPlantRepository = remember { UserPlantRepository(context) }
    val assetLoader = remember { AssetLoader() }

    var allPlantsList by remember { mutableStateOf<List<PlantData>>(emptyList()) }
    val userPlantIds by userPlantRepository.userPlantsFlow.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        launch {
            allPlantsList = assetLoader.loadPlantsFromAssets(context)
        }
    }

    val userPlants = remember(userPlantIds, allPlantsList) {
        allPlantsList.filter { plantData -> plantData.id in userPlantIds }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Destinations.SEARCH_PLANTS) }) {
                Icon(Icons.Filled.Add, contentDescription = "Add plant")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Text(
                text = "My Garden", // Changed title
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
            )

            if (userPlants.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Your garden is empty.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Tap the '+' button to add some plants!",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    items(userPlants) { plant ->
                        PlantCard(plant = plant)
                    }
                }
            }
        }
    }
}

// data class Plant(...) and val samplePlants = listOf(...) are removed or commented out.
// To avoid breaking the preview completely, we pass some simple PlantData
// For a real preview of DataStore content, more setup would be needed.
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    // This preview will show an empty garden initially as it doesn't mock DataStore/AssetLoader
    HomeScreen(navController = rememberNavController())
}