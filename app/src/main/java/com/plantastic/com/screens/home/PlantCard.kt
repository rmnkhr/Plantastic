package com.plantastic.com.screens.home

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import androidx.graphics.shapes.toPath
import coil.compose.AsyncImage
import com.plantastic.com.R
import com.plantastic.com.data.PlantData
import com.plantastic.com.shapes.RoundedPolygonShape
import com.plantastic.com.shapes.ShapeParameters
import com.plantastic.com.shapes.radialToCartesian
import com.plantastic.com.shapes.toRadians

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
    ) {
        val shape = ShapeParameters(
            sides = 8,
            innerRadius = .784f,
            roundness = .16f,
            shapeId = ShapeParameters.ShapeId.Star
        )

        val q = shape.genShape()

//        val infiniteAnimation = rememberInfiniteTransition(label = "infinite animation")
//        val morphProgress = infiniteAnimation.animateFloat(
//            initialValue = 0f,
//            targetValue = 1f,
//            animationSpec = infiniteRepeatable(
//                tween(500),
//                repeatMode = RepeatMode.Reverse
//            ),
//            label = "morph"
//        )
//        Box(
//            modifier = Modifier
//                .drawWithCache {
//                    val triangle = RoundedPolygon(
//                        numVertices = 3,
//                        radius = size.minDimension / 2f,
//                        centerX = size.width / 2f,
//                        centerY = size.height / 2f,
//                        rounding = CornerRounding(
//                            size.minDimension / 10f,
//                            smoothing = 0.1f
//                        )
//                    )
//                    val square = RoundedPolygon(
//                        numVertices = 6,
//                        radius = size.minDimension / 2f,
//                        centerX = size.width / 2f,
//                        centerY = size.height / 2f
//                    )
//
//                    val morph = Morph(start = triangle, end = square)
//                    val morphPath = morph
//                        .toPath(progress = morphProgress.value)
//                        .asComposePath()
//
//                    onDrawBehind {
//                        drawPath(morphPath, color = Color.Black)
//                    }
//                }
//                .size(200.dp)
//        )

        val hexagon = remember {
            RoundedPolygon.star(
                8,
                rounding = CornerRounding(0.2f)
            )
        }
        val clip = remember(hexagon) {
            RoundedPolygonShape(polygon = hexagon)
        }


        Column(modifier = Modifier.padding(8.dp)) {
            if (plant.imageName.startsWith("http")) {
                AsyncImage(
                    model = plant.imageName,
                    contentDescription = plant.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth()
                        .clip(clip)
                    ,
                    error = painterResource(id = R.drawable.ic_launcher_background)
                )
            } else {
                Image(
                    painter = if (imageResId != 0) painterResource(id = imageResId) else painterResource(
                        id = R.drawable.ic_launcher_background
                    ),
                    contentDescription = plant.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = plant.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            )
            {
                Image(
                    painter = painterResource(id = R.drawable.ic_water),
                    contentDescription = "Watering Icon",
                    modifier = Modifier
                        .height(24.dp)
                        .aspectRatio(1f)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Полив: ${plant.wateringNeeds}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 2,
                    minLines = 2
                )
            }
        }
    }
}

@Preview
@Composable
fun PlantCardPreview() {
    val samplePlant = PlantData(
        id = "1",
        name = "Plant Name",
        imageName = "",
        wateringNeeds = "Раз в три дні",
        lightNeeds = "Яскраве світло",
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        PlantCard(plant = samplePlant)
    }
}
