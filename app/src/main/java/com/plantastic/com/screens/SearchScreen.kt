package com.plantastic.com.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.plantastic.com.data.PlantData
import com.plantastic.com.data.UserPlantRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    allPlants: List<PlantData>,
    userPlantRepository: UserPlantRepository, // Added repository
    onPlantSelected: (PlantData) -> Unit // Kept for now, but primary action is via repository
) {
    var searchText by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope() // For launching repository actions

    val filteredPlants by remember(searchText, allPlants) {
        derivedStateOf {
            if (searchText.isBlank()) {
                allPlants
            } else {
                allPlants.filter { plant ->
                    plant.name.contains(searchText, ignoreCase = true)
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search for plants") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(filteredPlants) { plant ->
                ListItem(
                    headlineText = { Text(plant.name) },
                    supportingText = { Text("Light: ${plant.lightNeeds}, Water: ${plant.wateringNeeds}") },
                    modifier = Modifier.clickable {
                        coroutineScope.launch {
                            userPlantRepository.addPlant(plant.id)
                        }
                        onPlantSelected(plant) // Call original lambda if needed for other actions
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    val samplePlants = listOf(
        PlantData("1", "Snake Plant", "plant_image_1", "Low to bright, indirect light", "Water every 2-8 weeks"),
        PlantData("2", "Pothos", "plant_image_2", "Bright, indirect light", "Water every 1-2 weeks"),
        PlantData("3", "ZZ Plant", "plant_image_3", "Low to bright, indirect light", "Water every 2-3 weeks")
    )
    // For preview, we need a UserPlantRepository instance.
    // This might not be ideal for pure previews but works for seeing the layout.
    val context = LocalContext.current
    SearchScreen(
        allPlants = samplePlants,
        userPlantRepository = UserPlantRepository(context),
        onPlantSelected = {}
    )
}
