package com.plantastic.com.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.plantastic.com.R
import com.plantastic.com.vm.PlantDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantDetailScreen(
    navController: NavController,
    viewModel: PlantDetailViewModel,
    plantId: String?
) {
    LaunchedEffect(Unit) {
        Log.d("PlantDetailScreen", "PlantDetailScreen launched with plantId: $plantId")
        viewModel.loadPlantById(plantId ?: "")
    }
    val plant = viewModel.plant.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = plant?.name ?: "Unknown Plant", fontWeight = FontWeight.Bold)
                        Text(
                            text = plant?.scientificName ?: "",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle more options */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More options")
                    }
                }
            )
        },
        floatingActionButton = {
            Button(
                onClick = { /* Handle add to collection */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Add to collection")
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        plant?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.plant_image_1), // Replace with actual image
                    contentDescription = "Plant Image",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(24.dp))

                CareSection(plant = it)

                Spacer(modifier = Modifier.height(24.dp))

                AboutSection(plant = it)
            }
        }
    }
}

@Composable
fun CareSection(plant: com.plantastic.com.data.Plant) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text("Care", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        InfoRow(icon = Icons.Default.WaterDrop, text = plant.wateringFrequency)
        Spacer(modifier = Modifier.height(8.dp))
        InfoRow(icon = Icons.Default.Lightbulb, text = plant.lightRequirements)
        Spacer(modifier = Modifier.height(8.dp))
        InfoRow(icon = Icons.Default.Eco, text = plant.soilType)
        Spacer(modifier = Modifier.height(8.dp))
        InfoRow(icon = Icons.Default.Favorite, text = plant.fertilizing)
    }
}

@Composable
fun AboutSection(plant: com.plantastic.com.data.Plant) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text("About", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        InfoRow(icon = Icons.Default.Info, text = "Type: ${plant.plantType}")
        Spacer(modifier = Modifier.height(8.dp))
        InfoRow(icon = Icons.Default.Thermostat, text = "Environment: ${plant.idealEnvironment}")
        Spacer(modifier = Modifier.height(8.dp))
        InfoRow(icon = Icons.Default.Star, text = "Difficulty: ${plant.difficulty}")
        Spacer(modifier = Modifier.height(8.dp))
        InfoRow(icon = Icons.Default.Warning, text = "Toxicity: ${plant.toxicity}")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = plant.description, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun InfoRow(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}
