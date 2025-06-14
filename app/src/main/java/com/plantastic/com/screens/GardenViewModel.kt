package com.plantastic.com.screens

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.plantastic.com.data.Plant
import com.plantastic.com.data.PlantMood
import com.plantastic.com.data.PlantRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class GardenViewModel(application: Application) : ViewModel() {
    private val plantRepository = PlantRepository(application)

    val plants: Flow<List<Plant>> = plantRepository.plants

    private val _isInCall = MutableStateFlow(false)
    val isInCall: StateFlow<Boolean> = _isInCall.asStateFlow()

    private val _currentCallPlant = MutableStateFlow<Plant?>(null)
    val currentCallPlant: StateFlow<Plant?> = _currentCallPlant.asStateFlow()

    private val _callDuration = MutableStateFlow(0L)
    val callDuration: StateFlow<Long> = _callDuration.asStateFlow()

    private val _isPlayingNatureSounds = MutableStateFlow(false)
    val isPlayingNatureSounds: StateFlow<Boolean> = _isPlayingNatureSounds.asStateFlow()

    fun startCall(plant: Plant) {
        viewModelScope.launch {
            _currentCallPlant.value = plant
            _isInCall.value = true
            _callDuration.value = 0L
            // Змінюємо настрій рослини на EXCITED при початку дзвінка
            plantRepository.updatePlantMood(plant.id, PlantMood.EXCITED)
        }
    }

    fun endCall() {
        viewModelScope.launch {
            _currentCallPlant.value?.let { plant ->
                // Повертаємо настрій рослини до HAPPY після дзвінка
                plantRepository.updatePlantMood(plant.id, PlantMood.HAPPY)
            }
            _isInCall.value = false
            _currentCallPlant.value = null
            _callDuration.value = 0L
            _isPlayingNatureSounds.value = false
        }
    }

    fun toggleNatureSounds() {
        _isPlayingNatureSounds.value = !_isPlayingNatureSounds.value
    }

    fun updateCallDuration(duration: Long) {
        _callDuration.value = duration
    }

    fun addPlant(name: String, imageUri: String) {
        val plant = Plant(
            name = name,
            imageUri = imageUri,
            careTips = listOf(
                "Не забудь полити мене! 💧",
                "Я люблю світле місце 🌞",
                "Повертайся швидше! 🌱",
                "Я так скучив за тобою! 💚",
                "Розкажи мені про свій день 🌿"
            )
        )
        plantRepository.addPlant(plant)
    }

    fun deletePlant(plantId: String) {
        plantRepository.deletePlant(plantId)
    }

    fun updatePlant(plant: Plant) {
        plantRepository.updatePlant(plant)
    }
}

class GardenViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GardenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GardenViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 