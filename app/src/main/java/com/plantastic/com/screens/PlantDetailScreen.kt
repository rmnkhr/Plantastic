package com.plantastic.com.screens

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.AddHome
import androidx.compose.material.icons.filled.AddReaction
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.plantastic.com.R
import com.plantastic.com.data.Plant
import com.plantastic.com.shapes.CustomRotatingMorphShape
import com.plantastic.com.ui.WaveDivider
import com.plantastic.com.vm.PlantDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantDetailScreen(
    navController: NavController,
    viewModel: PlantDetailViewModel,
    plantId: String?
) {
    LaunchedEffect(Unit) {
        if (plantId != null) {
            viewModel.getPlantById(plantId)
        }
    }

    val plant = viewModel.plant.collectAsState().value
    if (plant != null) {
        PlantDetailScreenUI(navController, plant)
    }

}

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PlantDetailScreenUI(
    navController: NavController,
    plant: Plant
) {

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = {

            val isChecked = remember { mutableStateOf(false) }


            val materials = MaterialShapes.Bun

            val shapeA = remember {
                RoundedPolygon(
                    4,
                    rounding = CornerRounding(0.2f)
                )
            }
            val shapeB = remember {
                RoundedPolygon.star(
                    6,
                    innerRadius = 0.7f,
                    rounding = CornerRounding(0.1f)
                )
            }
            val morph = remember {
                Morph(materials, shapeB)
            }
//            val interactionSource = remember { MutableInteractionSource() }
//            val isPressed by interactionSource.collectIsPressedAsState()

            val animatedProgress = animateFloatAsState(
                targetValue = if (isChecked.value) 1f else 0f,
                label = "progress",
                animationSpec = spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessMedium)
            )

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
                    //.clickable(interactionSource = interactionSource, indication = null) {
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        isChecked.value = !isChecked.value
                    }
                    .clip(
                        CustomRotatingMorphShape(
                            morph,
                            animatedProgress.value,
                            animatedProgress.value * 90f
                        )
                    )
                    .background(MaterialTheme.colorScheme.primary)
//                onClick = {
//                    //isChecked.value = !isChecked.value
//                },
//                shape = MorphPolygonShape(morph, animatedProgress.value)

            ) {
                Icon(
                    imageVector = Icons.Default.AddAlert, contentDescription = "Add",
                    modifier = Modifier.align(Alignment.Center),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        topBar = {
            MediumTopAppBar(
                title = {
                    Column {
                        Text(text = plant.name, fontWeight = FontWeight.Bold)
                        Text(text = "Mood", style = MaterialTheme.typography.bodySmall)
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
                },
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButtonPosition = FabPosition.EndOverlay
    ) { padding ->

        val shapeA = remember {
            RoundedPolygon(
                12,
                rounding = CornerRounding(0.4f)
            )
        }
        val shapeB = remember {
            RoundedPolygon.star(
                16,
                rounding = CornerRounding(0.2f)
            )
        }
        val morph = remember {
            Morph(shapeA, shapeB)
        }

        val infiniteTransition = rememberInfiniteTransition("infinite outline movement")
        val animatedRotation = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                tween(12000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "animatedMorphProgress"
        )

        val q = Morph(shapeA, shapeB)

//        val animatedProgress = infiniteTransition.animateFloat(
//            initialValue = 0f,
//            targetValue = 1f,
//            animationSpec = infiniteRepeatable(
//                tween(4000, easing = LinearEasing),
//                repeatMode = RepeatMode.Reverse
//            ),
//            label = "animatedMorphProgress"
//        )

        val scrollState = rememberScrollState()
        val scrollProgress = remember {
            derivedStateOf {
                if (scrollState.maxValue == 0) 0f
                else scrollState.value.toFloat() / 700f
            }
        }


        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = plant.imageUri,
                    contentDescription = plant.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxWidth()
                        .clip(
                            CustomRotatingMorphShape(
                                morph,
                                scrollProgress.value,
                                animatedRotation.value
                            )
                        )
                        .background(Color.Gray),
                    error = painterResource(id = R.drawable.ic_launcher_background)
                )

                Spacer(modifier = Modifier.height(80.dp))

                WaveDivider(
                    bottomColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                )

                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    CareSection()

                    Spacer(modifier = Modifier.height(224.dp))

                    AboutSection()
                    Spacer(modifier = Modifier.height(224.dp))
                    AboutSection()
                    Spacer(modifier = Modifier.height(224.dp))
                    AboutSection()
                }
            }

            Box(
                modifier = Modifier
                    .padding(32.dp)
                    .width(80.dp)
                    .align(Alignment.CenterEnd)
                    .height(320.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceBright.copy(alpha = 0.7f),
                        shape = CircleShape
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {



                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Center),
                        tint = MaterialTheme.colorScheme.onPrimary
                    ) }
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.WaterDrop,
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.Center),
                            tint = MaterialTheme.colorScheme.onPrimary
                        ) }
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.WaterDrop,
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.Center),
                            tint = MaterialTheme.colorScheme.onPrimary
                        ) }
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                shape = RoundedPolygon.star(
                                    8,
                                    innerRadius = 0.4f,
                                    innerRounding = CornerRounding(0.4f,0.7f),
                                    rounding = CornerRounding(0.8f)
                                ).toShape()
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.WaterDrop,
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.Center),
                            tint = MaterialTheme.colorScheme.onPrimary
                        ) }
                }
            }
        }
    }
}

@Composable
fun CareSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text("Care", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        InfoRow(icon = Icons.Default.WaterDrop, text = "Water every Tuesday")
        Spacer(modifier = Modifier.height(8.dp))
        InfoRow(icon = Icons.Default.Fastfood, text = "Feed once monthly")
    }
}

@Composable
fun AboutSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text("About", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        InfoRow(icon = Icons.Default.AddReaction, text = "Moderate light")
        Spacer(modifier = Modifier.height(8.dp))
        InfoRow(icon = Icons.Default.LocationOn, text = "Slightly dry, well-draining soil")
        Spacer(modifier = Modifier.height(8.dp))
        InfoRow(icon = Icons.Default.AddHome, text = "Office windowsill")
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

@Preview
@Composable
fun PlantDetailScreenPreview() {
    PlantDetailScreenUI(
        navController = rememberNavController(),
        plant = Plant(
            name = "Rose",
        )
    )
}

