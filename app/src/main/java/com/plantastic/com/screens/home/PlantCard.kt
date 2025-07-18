package com.plantastic.com.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.plantastic.com.R
import com.plantastic.com.data.PlantData

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
                    error = painterResource(id = R.drawable.ic_launcher_background)
                )
            } else {
                Image(
                    painter = if (imageResId != 0) painterResource(id = imageResId) else painterResource(id = R.drawable.ic_launcher_background),
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

@Preview
@Composable
fun PlantCardPreview() {
    val samplePlant = PlantData(
        id = "1",
        name = "",
        imageName = "",
        wateringNeeds = "Раз в неделю",
        lightNeeds  = "Яскраве світло",
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .height(200.dp)
    ) {
        PlantCard(plant = samplePlant)
    }
}
