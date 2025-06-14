package com.plantastic.com.screens

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantastic.com.data.ThemeSettingsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditProfileViewModel(application: Application) : ViewModel() {

    private val themeSettingsManager = ThemeSettingsManager(application)

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _avatarUri = MutableStateFlow("")
    val avatarUri: StateFlow<String> = _avatarUri.asStateFlow()

    fun loadProfile() {
        viewModelScope.launch {
            _name.value = themeSettingsManager.userNameFlow.first()
            _email.value = themeSettingsManager.userEmailFlow.first()
            _avatarUri.value = themeSettingsManager.avatarUriFlow.first()
        }
    }

    fun updateName(newName: String) {
        _name.value = newName
    }

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun updateAvatarUri(newUri: String) {
        _avatarUri.value = newUri
        // In a real app, this might trigger an image picker,
        // and the result would update the URI.
        // For now, we directly set it.
    }

    fun saveProfile() {
        viewModelScope.launch {
            themeSettingsManager.setUserName(_name.value)
            themeSettingsManager.setUserEmail(_email.value)
            themeSettingsManager.setAvatarUri(_avatarUri.value)
        }
    }
}
