package com.plantastic.com.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.plantastic.com.data.Plant
import com.plantastic.com.data.PlantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlantDetailViewModel(
    application: Application
) : ViewModel() {
    private val plantRepository = PlantRepository(application.applicationContext)
    private val _plant = MutableStateFlow<Plant?>(null)
    val plant: StateFlow<Plant?> = _plant.asStateFlow()

    fun loadPlantById(plantId: String) {
        viewModelScope.launch {

            val foundPlant = plantRepository.getPlant(plantId)
            Log.d("PlantDetailViewModel", "Plant loaded: $foundPlant")
            _plant.value = foundPlant
        }
    }
}

class PlantDetailViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlantDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlantDetailViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}