package com.plantastic.com.data

import java.util.UUID

data class Notification(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val frequency: Int // Represents frequency in days
)
