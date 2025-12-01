package com.proyectoforocine.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.proyectoforocine.data.UserPreferencesRepository
import com.proyectoforocine.model.PerfilUiState
import com.proyectoforocine.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PerfilViewModel(application: Application) : AndroidViewModel(application) {
    
    private val userPreferencesRepository = UserPreferencesRepository(application)
    
    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()
    
    // Cargar perfil desde DataStore
    val userProfile: StateFlow<UserProfile> = userPreferencesRepository.userProfile.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserProfile()
    )
    
    init {
        viewModelScope.launch {
            userProfile.collect { profile ->
                _uiState.update { it.copy(profile = profile) }
            }
        }
    }
    
    fun onNombreChange(nombre: String) {
        viewModelScope.launch {
            userPreferencesRepository.saveUserName(nombre)
        }
    }
    
    fun onFotoSeleccionada(uri: Uri?) {
        viewModelScope.launch {
            userPreferencesRepository.saveUserPhoto(uri?.toString())
        }
    }
    
    fun onModoOscuroToggle(enabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveDarkMode(enabled)
        }
    }
    
    fun onNotificacionesToggle(enabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveNotificationsEnabled(enabled)
        }
    }
    
    fun onShowImageSourceDialog() {
        _uiState.update { it.copy(showImageSourceDialog = true) }
    }
    
    fun onHideImageSourceDialog() {
        _uiState.update { it.copy(showImageSourceDialog = false) }
    }
}