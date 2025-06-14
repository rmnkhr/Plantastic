package com.plantastic.com.utils

import android.content.Context
import com.plantastic.com.data.PlantData
import kotlinx.serialization.json.Json
import java.io.IOException

class AssetLoader {

    suspend fun loadPlantsFromAssets(context: Context): List<PlantData> {
        return try {
            val jsonString = context.assets.open("plants.json").bufferedReader().use { it.readText() }
            Json.decodeFromString<List<PlantData>>(jsonString)
        } catch (e: IOException) {
            // Log the error or handle it as needed
            e.printStackTrace()
            emptyList()
        }
    }
}
