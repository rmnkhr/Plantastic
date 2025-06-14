package com.plantastic.com.screens

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantastic.com.data.ThemeSetting
import com.plantastic.com.data.ThemeSettingsManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : ViewModel() {

    private val themeSettingsManager = ThemeSettingsManager(application)

    // Theme settings
    val themeSettingFlow: Flow<ThemeSetting> = themeSettingsManager.themeSettingFlow

    fun setTheme(theme: ThemeSetting) {
        viewModelScope.launch {
            themeSettingsManager.setTheme(theme)
        }
    }

    // User profile data
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _avatarUri = MutableStateFlow("")
    val avatarUri: StateFlow<String> = _avatarUri.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _name.value = themeSettingsManager.userNameFlow.first()
            _email.value = themeSettingsManager.userEmailFlow.first()
            _avatarUri.value = themeSettingsManager.avatarUriFlow.first()
        }
    }
}
