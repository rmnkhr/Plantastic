package com.plantastic.com.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunDetailsScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Running Details") },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: Navigate back */ }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "More options")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // User and Map
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = "User Avatar",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(12.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Dimitri Litvinov", fontWeight = FontWeight.Bold)
                        Text("6:55am • Miami, FL", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Spacer(Modifier.height(16.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Map Placeholder", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            // Main Stats
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    StatColumn("Distance", "3.50km")
                    StatColumn("Avg. Pace", "5:20/km")
                    StatColumn("Elevation Gain", "100m")
                    StatColumn("Calories", "370Cal")
                }
            }

            // Milestones
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    MilestoneCard("1 mile", "4:18/km", isSpecial = false)
                    MilestoneCard("3 mile", "5:28/km", isSpecial = true) // Special achievement
                    MilestoneCard("4 mile", "3:58/km", isSpecial = false)
                }
            }

            // Pace Analysis
            item { PaceAnalysisCard() }

            // See Full Report Button
            item {
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Text("See full report", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}


@Composable
fun StatColumn(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun MilestoneCard(title: String, pace: String, isSpecial: Boolean) {
    val containerColor = if (isSpecial) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.surface
    val contentColor = if (isSpecial) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onSurface

    Card(
        modifier = Modifier.size(width = 100.dp, height = 100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Filled.Star, contentDescription = "Milestone star", tint = contentColor)
            Spacer(Modifier.height(4.dp))
            Text(title, fontWeight = FontWeight.Bold, color = contentColor)
            Text(pace, style = MaterialTheme.typography.bodySmall, color = contentColor.copy(alpha = 0.8f))
        }
    }
}

@Composable
private fun PaceAnalysisCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Pace Analysis", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(16.dp))
            // Simplified bar chart representation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                val paces = listOf(0.4f, 0.5f, 0.7f, 0.65f, 0.8f, 0.75f, 0.5f, 0.6f, 0.4f, 0.7f, 0.9f, 1.0f)
                paces.forEach { pace ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(pace)
                            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }
            }
        }
    }
}