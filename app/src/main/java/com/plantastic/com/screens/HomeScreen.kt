package com.plantastic.com.screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen() {
    Text("🏠 Home", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(16.dp))
}