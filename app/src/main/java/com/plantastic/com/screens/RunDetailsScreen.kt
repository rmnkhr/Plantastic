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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen() {
    Scaffold(
        bottomBar = {
            // "Start your run" button is styled to look like the design
            Button(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(

                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Start Run",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Start your run",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Weekly overview", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Runs — 03", style = MaterialTheme.typography.bodyLarge)
                    Text("Time — 2:13h", style = MaterialTheme.typography.bodyLarge)
                    Text("Distance — 12km", style = MaterialTheme.typography.bodyLarge)
                }
                Spacer(modifier = Modifier.height(16.dp))
                DayOfWeekTabs()
            }

            // Daily Prompt Card
            item { DailyPromptCard() }

            // Meal and Vitamin Cards
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    MealPlanCard(modifier = Modifier.weight(1f))
                    VitaminsCard(modifier = Modifier.weight(1f))
                }
            }

            // Runs this week
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Person, contentDescription = "Runs Icon")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Runs this week", style = MaterialTheme.typography.titleMedium)
                }
            }

            // Run items
            item { RunHistoryCard("Morning run", "6:55am", "3.50km", "5:20/km", "18m41s") }
            item { RunHistoryCard("Sunset run", "8:34pm", "4.00km", "5:03/km", "22m13s") }

            item { Spacer(modifier = Modifier.height(16.dp)) } // Spacer for bottom button
        }
    }
}

@Composable
private fun DayOfWeekTabs() {
    val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday")
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(days) { day ->
            val isSelected = day == "Tuesday"
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = day,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun DailyPromptCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
            Column(Modifier.weight(1f)) {
                Text("Thursday, July 13th", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("How're you feeling today? I can determine best run option for you.")
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.Close, contentDescription = "Close prompt")
            }
        }
    }
}

@Composable
private fun MealPlanCard(modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(16.dp)) {
            Text("Meal for today", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("10:00am Avocado toast")
            Text("12:00pm Lettuce wrap")
        }
    }
}

@Composable
private fun VitaminsCard(modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(16.dp)) {
            Text("Vitamins for today", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("10:00am Magnesium")
            Text("12:00pm Potassium")
        }
    }
}

@Composable
private fun RunHistoryCard(title: String, timeOfDay: String, distance: String, pace: String, time: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Person, contentDescription = "User avatar", modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(8.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(title, fontWeight = FontWeight.Bold)
                    Text(timeOfDay, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Box(
                    modifier = Modifier
                        .height(80.dp)
                        .width(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Map", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatColumn("Distance", distance)
                StatColumn("Pace", pace)
                StatColumn("Time", time)
            }
        }
    }
}