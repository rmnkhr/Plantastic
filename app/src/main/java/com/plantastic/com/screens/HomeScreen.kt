package com.plantastic.com.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.plantastic.com.Destinations
import com.plantastic.com.data.PlantData
import com.plantastic.com.data.UserPlantRepository
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
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "My Garden", // Changed title
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
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
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(userPlants) { plant ->
                        PlantCard(plant = plant)
                    }
                }
            }
        }
    }
}

@Composable
fun PlantCard(plant: PlantData) {
    val context = LocalContext.current
    val imageResId = remember(plant.imageName) {
        if (!plant.imageName.startsWith("http")) {
            context.resources.getIdentifier(plant.imageName, "drawable", context.packageName)
        } else 0
    }

    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            if (plant.imageName.startsWith("http")) {
                AsyncImage(
                    model = plant.imageName,
                    contentDescription = plant.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium),
                    error = painterResource(id = com.plantastic.com.R.drawable.ic_launcher_background)
                )
            } else {
                Image(
                    painter = if (imageResId != 0) painterResource(id = imageResId) else painterResource(id = com.plantastic.com.R.drawable.ic_launcher_background),
                    contentDescription = plant.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = plant.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Полив: ${plant.wateringNeeds}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
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

/*
// Old Plant data class and sample data - can be removed
data class Plant(
    val name: String,
    val description: String,
    val imageRes: Int
)

val samplePlants = listOf(
    Plant("Snake Plant", "Low maintenance, great for beginners.", R.drawable.arabica_plant_image),
    Plant("Pothos", "Thrives in low light, perfect for indoors.", R.drawable.arabica_plant_image),
    Plant("Spider Plant", "Air-purifying and easy to care for.", R.drawable.arabica_plant_image),
    Plant("Peace Lily", "Beautiful blooms, needs moderate light.", R.drawable.arabica_plant_image)
)
*/