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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.plantastic.com.Destinations
import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon // Keep for default icon if needed, but AsyncImage is primary
import androidx.compose.foundation.layout.size
import com.plantastic.com.data.ThemeSetting
import coil.compose.AsyncImage
import android.net.Uri
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.plantastic.com.R // Assuming R class for drawables

// Enum ThemeSetting was moved to data package

@Composable
fun UserProfileHeader(
    name: String,
    email: String,
    avatarUri: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AsyncImage(
            model = if (avatarUri.isEmpty()) painterResource(id = R.drawable.ic_launcher_foreground) else Uri.parse(avatarUri),
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.ic_launcher_foreground), // Default placeholder
            error = painterResource(id = R.drawable.ic_launcher_foreground) // Error placeholder
        )
        Text(
            text = if (name.isNotEmpty()) name else "User Name",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = if (email.isNotEmpty()) email else "user.email@example.com",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ThemeSwitcherSection(
    currentTheme: ThemeSetting,
    onThemeSelected: (ThemeSetting) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Select Theme", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp, top = 8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            enumValues<ThemeSetting>().forEach { theme ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = currentTheme == theme,
                        onClick = { onThemeSelected(theme) }
                    )
                    Text(theme.name, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(LocalContext.current.applicationContext as Application)
    )
) {
    val currentTheme by viewModel.themeSettingFlow.collectAsState(initial = ThemeSetting.LIGHT)
    val name by viewModel.name.collectAsState()
    val email by viewModel.email.collectAsState()
    val avatarUri by viewModel.avatarUri.collectAsState()

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

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        UserProfileHeader(name = name, email = email, avatarUri = avatarUri)

        ThemeSwitcherSection(
            currentTheme = currentTheme,
            onThemeSelected = { viewModel.setTheme(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(menuItems) { (title, route) ->
                Text(
                    text = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate(route) }
                        .padding(vertical = 12.dp)
                )
            }
        }
    }
}

// Simple ViewModel Factory for ProfileViewModel
class ProfileViewModelFactory(private val application: Application) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}