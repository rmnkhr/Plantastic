package com.plantastic.com.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlantRepository(private val context: Context) {
    private val gson = Gson()
    private val _plants = MutableStateFlow<List<Plant>>(emptyList())
    val plants: Flow<List<Plant>> = _plants.asStateFlow()

    init {
        loadPlantsFromJson()
    }

    private fun loadPlantsFromJson() {
        try {
            val jsonString = context.assets.open("plants.json").bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<Plant>>() {}.type
            _plants.value = gson.fromJson(jsonString, type)
        } catch (e: Exception) {
            e.printStackTrace()
            _plants.value = emptyList()
        }
    }

    fun addPlant(plant: Plant) {
        _plants.value = _plants.value + plant
    }

    fun updatePlant(plant: Plant) {
        _plants.value = _plants.value.map { if (it.id == plant.id) plant else it }
        savePlants()
    }

    fun deletePlant(plantId: String) {
        _plants.value = _plants.value.filter { it.id != plantId }
        savePlants()
    }

    fun getPlant(plantId: String): Plant? {
        return _plants.value.find { it.id == plantId }
    }

    fun updatePlantMood(plantId: String, mood: PlantMood) {
        val plant = getPlant(plantId) ?: return
        updatePlant(plant.copy(mood = mood))
    }

    fun updateLastWatered(plantId: String) {
        val plant = getPlant(plantId) ?: return
        updatePlant(plant.copy(lastWatered = System.currentTimeMillis()))
    }
} 