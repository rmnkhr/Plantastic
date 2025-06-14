package com.plantastic.com.data

import kotlinx.serialization.Serializable

@Serializable
data class PlantData(
    val id: String,
    val name: String,
    val imageName: String,
    val lightNeeds: String,
    val wateringNeeds: String
)
