package com.proyectoforocine.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyectoforocine.data.ForoRepository
import com.proyectoforocine.data.local.Tema
import com.proyectoforocine.data.local.UsuarioEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

data class PerfilUiState(
    val usuario: UsuarioEntity? = null,
    val temasUsuario: List<Tema> = emptyList(),
    val isLoading: Boolean = true
)

class PerfilViewModel(private val repository: ForoRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    fun loadUserProfile(userId: Long) {
        viewModelScope.launch {
            val userFlow = repository.getUsuarioPorId(userId).filterNotNull()
            val userTemasFlow = repository.getTemasPorAutorId(userId)

            userFlow.combine(userTemasFlow) { usuario, temas ->
                PerfilUiState(usuario = usuario, temasUsuario = temas, isLoading = false)
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
}