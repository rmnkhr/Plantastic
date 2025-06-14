package com.plantastic.com.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlantRepository(private val context: Context) {
    private val gson = Gson()
    private val prefsName = "plants_prefs"
    private val plantsKey = "plants_list"

    private val _plants = MutableStateFlow<List<Plant>>(emptyList())
    val plants: Flow<List<Plant>> = _plants.asStateFlow()

    init {
        loadPlants()
    }

    private fun loadPlants() {
        val sharedPrefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val jsonPlants = sharedPrefs.getString(plantsKey, null)
        _plants.value = if (jsonPlants != null) {
            val type = object : TypeToken<List<Plant>>() {}.type
            gson.fromJson(jsonPlants, type)
        } else {
            emptyList()
        }
    }

    private fun savePlants() {
        val sharedPrefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val jsonPlants = gson.toJson(_plants.value)
        sharedPrefs.edit().putString(plantsKey, jsonPlants).apply()
    }

    fun addPlant(plant: Plant) {
        _plants.value = _plants.value + plant
        savePlants()
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