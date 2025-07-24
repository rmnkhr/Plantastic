package com.plantastic.com.data

import android.net.Uri
import java.util.UUID

data class Plant(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val scientificName: String,
    val imageUri: String = "", // URI для зображення рослини
    val plantType: String,
    val idealEnvironment: String,
    val difficulty: String,
    val toxicity: String,
    val wateringFrequency: String,
    val lightRequirements: String,
    val soilType: String,
    val fertilizing: String,
    val description: String,
    val lastWatered: Long = System.currentTimeMillis(),
    val wateringInterval: Int = 7, // в днях
    val mood: PlantMood = PlantMood.HAPPY,
    val careTips: List<String> = emptyList()
) {
    fun getRandomCareTip(): String {
        val careTips = listOf(
            "Поливай мене регулярно, але не перестарайся!",
            "Мені потрібно багато світла, але не прямого сонця.",
            "Не забудь перевірити вологість ґрунту.",
            "Можливо, мені потрібна підживка?",
            "Оберіть мене проти годинникової стрілки для рівномірного росту.",
            "Перевірте, чи немає на мені шкідників.",
            "Мені подобається, коли ти розмовляєш зі мною!",
            "Не забудь прочистити мої листочки від пилу.",
            "Можливо, мені потрібен більший горщик?",
            "Я люблю, коли в кімнаті тепло і вологисто."
        )
        return careTips.random()
    }
}

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