package com.plantastic.com.screens

import android.app.Application
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.plantastic.com.data.Plant
import com.plantastic.com.data.PlantMood
import com.plantastic.com.data.PlantRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

enum class CallState {
    CALLING,    // Стан виклику
    CONNECTING, // Стан підключення
    IN_CALL     // Стан активного дзвінка
}

class GardenViewModel(
    private val application: Application
) : ViewModel() {
    private val plantRepository = PlantRepository(application.applicationContext)
    private val _plants = MutableStateFlow<List<Plant>>(emptyList())
    val plants: StateFlow<List<Plant>> = _plants.asStateFlow()

    private val _isInCall = MutableStateFlow(false)
    val isInCall: StateFlow<Boolean> = _isInCall.asStateFlow()

    private val _currentCallPlant = MutableStateFlow<Plant?>(null)
    val currentCallPlant: StateFlow<Plant?> = _currentCallPlant.asStateFlow()

    private val _callDuration = MutableStateFlow(0L)
    val callDuration: StateFlow<Long> = _callDuration.asStateFlow()

    private val _isPlayingNatureSounds = MutableStateFlow(false)
    val isPlayingNatureSounds: StateFlow<Boolean> = _isPlayingNatureSounds.asStateFlow()

    private val _callState = MutableStateFlow(CallState.CALLING)
    val callState: StateFlow<CallState> = _callState.asStateFlow()

    private val _currentCareTip = MutableStateFlow("")
    val currentCareTip: StateFlow<String> = _currentCareTip.asStateFlow()

    private var callStartTime: Long = 0

    init {
        viewModelScope.launch {
            plantRepository.plants.collect { plantsList ->
                _plants.value = plantsList
            }
        }
    }

    fun startCall(plant: Plant) {
        viewModelScope.launch {
            _isInCall.value = true
            _currentCallPlant.value = plant
            _callDuration.value = 0
            _callState.value = CallState.CALLING
            _currentCareTip.value = plant.getRandomCareTip()
            callStartTime = System.currentTimeMillis()

            // Змінюємо стан на CONNECTING через 2 секунди
            delay(2000)
            _callState.value = CallState.CONNECTING

            // Змінюємо стан на IN_CALL через ще 1 секунду
            delay(1000)
            _callState.value = CallState.IN_CALL

            // Запускаємо оновлення порад кожні 5 секунд
            while (_callState.value == CallState.IN_CALL) {
                delay(5000)
                _currentCareTip.value = plant.getRandomCareTip()
            }
        }
    }

    fun endCall() {
        viewModelScope.launch {
            _isInCall.value = false
            _currentCallPlant.value = null
            _callDuration.value = 0
            _callState.value = CallState.CALLING
            _currentCareTip.value = ""
            _isPlayingNatureSounds.value = false
        }
    }

    fun updateCallDuration() {
        if (_callState.value == CallState.IN_CALL) {
            _callDuration.value = (System.currentTimeMillis() - callStartTime) / 1000
        }
    }

    fun toggleNatureSounds() {
        _isPlayingNatureSounds.value = !_isPlayingNatureSounds.value
    }

    fun addPlant(name: String, imageUri: String = "") {
        val newPlant = Plant(name = name, imageUri = imageUri)
        plantRepository.addPlant(newPlant)
    }

    fun deletePlant(plant: Plant) {
        plantRepository.deletePlant(plant.id)
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