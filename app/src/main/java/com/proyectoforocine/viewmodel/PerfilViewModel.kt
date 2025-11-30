package com.proyectoforocine.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyectoforocine.data.ForoRepository
import com.proyectoforocine.data.local.Tema
import com.proyectoforocine.data.local.UsuarioEntity
import com.proyectoforocine.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PerfilUiStateNew(
    val usuario: UsuarioEntity? = null,
    val temasUsuario: List<Tema> = emptyList(),
    val profile: UserProfile = UserProfile(),
    val isLoading: Boolean = true,
    val showImageSourceDialog: Boolean = false
)

class PerfilViewModel(private val repository: ForoRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilUiStateNew())
    val uiState: StateFlow<PerfilUiStateNew> = _uiState.asStateFlow()

    fun loadUserProfile(userId: Long) {
        viewModelScope.launch {
            val userFlow = repository.getUsuarioPorId(userId).filterNotNull()
            val userTemasFlow = repository.getTemasPorAutorId(userId)

            userFlow.combine(userTemasFlow) { usuario, temas ->
                _uiState.value.copy(
                    usuario = usuario,
                    temasUsuario = temas,
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    // Funciones para cámara y galería
    fun onFotoSeleccionada(uri: Uri?) {
        _uiState.update {
            it.copy(profile = it.profile.copy(fotoUri = uri?.toString()))
        }
    }

    fun onModoOscuroToggle(enabled: Boolean) {
        _uiState.update {
            it.copy(profile = it.profile.copy(modoOscuro = enabled))
        }
    }

    fun onNotificacionesToggle(enabled: Boolean) {
        _uiState.update {
            it.copy(profile = it.profile.copy(recibirNotificaciones = enabled))
        }
    }

    fun onShowImageSourceDialog() {
        _uiState.update { it.copy(showImageSourceDialog = true) }
    }

    fun onHideImageSourceDialog() {
        _uiState.update { it.copy(showImageSourceDialog = false) }
    }
}