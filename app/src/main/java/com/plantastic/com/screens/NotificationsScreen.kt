package com.plantastic.com.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.navigation.NavController
import com.plantastic.com.Destinations
import com.plantastic.com.data.Notification
import com.plantastic.com.data.NotificationRepository

@Composable
fun NotificationsScreen(navController: NavController) {
    val context = LocalContext.current
    val repository = remember { NotificationRepository() }
    var notifications by remember { mutableStateOf<List<Notification>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    fun refreshNotifications() {
        coroutineScope.launch {
            notifications = repository.getNotifications(context)
        }
    }

    // Load notifications when the screen is first composed
    LaunchedEffect(Unit) {
        refreshNotifications()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Destinations.ADD_NOTIFICATION) }) { // Navigate on click
                Icon(Icons.Filled.Add, "Add Notification")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                "🔔 Notifications",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
            LazyColumn(
                contentPadding = PaddingValues(all = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (notifications.isEmpty()) {
                    item {
                        Text(
                            text = "No notifications yet. Tap the '+' button to add one!",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp).fillMaxWidth()
                        )
                    }
                } else {
                    items(notifications, key = { it.id }) { notification ->
                        NotificationItem(
                            notification = notification,
                            onCancel = {
                                repository.cancelNotification(context, notification)
                                refreshNotifications() // Refresh list after cancelling
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationItem(notification: Notification, onCancel: () -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = notification.title, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.padding(top = 4.dp))
                Text(text = notification.description, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.padding(top = 4.dp))
                Text(text = "Remind every ${notification.frequency} days", style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = onCancel) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Cancel Notification",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}