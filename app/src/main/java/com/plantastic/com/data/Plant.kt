package com.plantastic.com.data

import android.net.Uri
import java.util.UUID

data class Plant(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val imageUri: String = "", // URI для зображення рослини
    val lastWatered: Long = System.currentTimeMillis(),
    val wateringInterval: Int = 7, // в днях
    val mood: PlantMood = PlantMood.HAPPY,
    val careTips: List<String> = emptyList()
)

enum class PlantMood {
    HAPPY,
    THIRSTY,
    SLEEPY,
    EXCITED,
    CONTENT
}

// Розширення для отримання випадкової поради по догляду
fun Plant.getRandomCareTip(): String {
    return careTips.randomOrNull() ?: when (mood) {
        PlantMood.THIRSTY -> "Я хочу пити! Полий мене, будь ласка 🌱"
        PlantMood.SLEEPY -> "Я трохи втомився... Давай відпочинемо разом 😴"
        PlantMood.EXCITED -> "Я так радий тебе бачити! Як твій день? 🌿"
        PlantMood.CONTENT -> "Все чудово! Дякую за турботу 💚"
        PlantMood.HAPPY -> "Я щасливий рости разом з тобою! 🌺"
    }
} 