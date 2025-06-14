package com.plantastic.com.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.RadioButton
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.plantastic.com.Destinations
import androidx.compose.foundation.clickable

// 1. Define Theme Options
enum class ThemeSetting {
    DYNAMIC, DARK, LIGHT
}

@Composable
fun ThemeSwitcherSection() {
    val currentTheme = remember { mutableStateOf(ThemeSetting.LIGHT) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Select Theme", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ThemeSetting.values().forEach { theme ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = currentTheme.value == theme,
                        onClick = { currentTheme.value = theme }
                    )
                    Text(theme.name, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(navController: NavController) { // Added NavController
    // Define menu items as a list of pairs: (Text, Destination Route)
    val menuItems = listOf(
        "Onboarding" to Destinations.ONBOARDING,
        "Privacy Policy" to Destinations.PRIVACY_POLICY,
        "Terms and Conditions" to Destinations.TERMS_AND_CONDITIONS,
        "Licenses" to Destinations.LICENSES,
        "Rate the App" to Destinations.RATE_THE_APP,
        "Statistics" to Destinations.STATISTICS,
        "Edit Profile" to Destinations.EDIT_PROFILE
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text("👤 Profile", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 16.dp))

        ThemeSwitcherSection()

        Spacer(modifier = Modifier.height(16.dp)) // Add some space before the list

        LazyColumn {
            items(menuItems) { (title, route) ->
                Text(
                    text = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate(route) }
                        .padding(vertical = 12.dp) // Increased padding for better touch target
                )
            }
        }
    }
}