package com.plantastic.com.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_plants")

class UserPlantRepository(private val context: Context) {

    private val USER_PLANTS_KEY = stringSetPreferencesKey("user_plant_ids")

    val userPlantsFlow: Flow<List<String>> = context.dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[USER_PLANTS_KEY]?.toList() ?: emptyList()
        }

    suspend fun addPlant(plantId: String) {
        context.dataStore.edit { preferences ->
            val currentPlantIds = preferences[USER_PLANTS_KEY] ?: emptySet()
            preferences[USER_PLANTS_KEY] = currentPlantIds + plantId
        }
    }

    suspend fun removePlant(plantId: String) {
        context.dataStore.edit { preferences ->
            val currentPlantIds = preferences[USER_PLANTS_KEY] ?: emptySet()
            preferences[USER_PLANTS_KEY] = currentPlantIds - plantId
        }
    }
}
