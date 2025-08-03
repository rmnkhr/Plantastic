package com.plantastic.com.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.Morph
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.plantastic.com.Destinations
import com.plantastic.com.data.PlantData
import com.plantastic.com.data.UserPlantRepository
import com.plantastic.com.screens.home.PlantCard
import com.plantastic.com.utils.AssetLoader
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
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

    val allShapes = with(MaterialShapes) {
        listOf(
            Circle, Square, Slanted, Arch, Arrow, SemiCircle, Oval, Pill, Triangle, Diamond, ClamShell,
            Pentagon, Gem, Sunny, VerySunny, Cookie4Sided, Cookie6Sided, Cookie7Sided, Cookie9Sided,
            Cookie12Sided, Ghostish, Clover4Leaf, Clover8Leaf, Burst, SoftBurst, Boom, SoftBoom,
            Flower, Puffy, PuffyDiamond, Bun
        )
    }

    val q1 = allShapes.get(0).normalized()
    val q2 = allShapes.get(1).normalized()

    val q = Morph(q1, q2)

    val userPlants = remember(userPlantIds, allPlantsList) {
        allPlantsList.filter { plantData -> plantData.id in userPlantIds }
    }

    val hazeState = rememberHazeState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.largeTopAppBarColors(Color.Transparent),
                modifier = Modifier
                    .hazeEffect(state = hazeState, style = HazeMaterials.ultraThin())
                    .fillMaxWidth(),
                title = {
                    Text("My Garden")
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Destinations.SEARCH_PLANTS) }) {
                Icon(Icons.Filled.Add, contentDescription = "Add plant")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(state = hazeState)
        ) {

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
                    contentPadding =
                        PaddingValues(
                            start = 16.dp,
                            top = paddingValues.calculateTopPadding(),
                            end = 16.dp,
                            bottom = 32.dp
                        ),
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                ) {
                    items(userPlants) { plant ->
                        PlantCard(
                            onPlantClick = { id ->
                                navController.navigate("plant_detail/$id")
                            },
                            plant = plant
                        )
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