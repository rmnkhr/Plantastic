package com.plantastic.com.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.plantastic.com.data.ThemeSetting // Corrected import for ThemeSetting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// preferencesDataStore extension defined at the top level
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ThemeSettingsManager(private val context: Context) {
    private val themeKey = stringPreferencesKey("theme_preference")

    // Profile Data Keys
    private val userNameKey = stringPreferencesKey("user_name")
    private val userEmailKey = stringPreferencesKey("user_email")
    private val avatarUriKey = stringPreferencesKey("avatar_uri")

    val themeSettingFlow: Flow<ThemeSetting> = context.dataStore.data
        .map { preferences ->
            val themeName = preferences[themeKey] ?: ThemeSetting.LIGHT.name
            try {
                ThemeSetting.valueOf(themeName)
            } catch (e: IllegalArgumentException) {
                // Handle unknown theme name, default to LIGHT
                ThemeSetting.LIGHT
            }
        }

    suspend fun setTheme(theme: ThemeSetting) {
        context.dataStore.edit { settings ->
            settings[themeKey] = theme.name
        }
    }

    // Profile Data Flows
    val userNameFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[userNameKey] ?: ""
        }

    val userEmailFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[userEmailKey] ?: ""
        }

    val avatarUriFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[avatarUriKey] ?: ""
        }

    // Profile Data Setters
    suspend fun setUserName(name: String) {
        context.dataStore.edit { settings ->
            settings[userNameKey] = name
        }
    }

    suspend fun setUserEmail(email: String) {
        context.dataStore.edit { settings ->
            settings[userEmailKey] = email
        }
    }

    suspend fun setAvatarUri(uri: String) {
        context.dataStore.edit { settings ->
            settings[avatarUriKey] = uri
        }
    }
}
