package com.proyectoforocine.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.proyectoforocine.data.UserPreferencesRepository
import com.proyectoforocine.model.TemaForo
import com.proyectoforocine.model.Usuario
import com.proyectoforocine.repository.ForoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Estado para el formulario de creación
data class CrearTemaUiState(
    val titulo: String = "",
    val contenido: String = "",
    val errorTitulo: String? = null
)

class ForoViewModel(application: Application) : AndroidViewModel(application) {

    // Repositorio para guardar/leer el rol
    private val userPreferencesRepository = UserPreferencesRepository(application)

    // --- ESTADO GENERAL ---
    val temas = ForoRepository.temas
    val rolUsuario: StateFlow<String?> = userPreferencesRepository.userRole.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    // --- ESTADO PANTALLA DETALLE ---
    private val _temaSeleccionado = MutableStateFlow<TemaForo?>(null)
    val temaSeleccionado = _temaSeleccionado.asStateFlow()

    // --- ESTADO PANTALLA CREACIÓN (NO LO USAREMOS EN ESTA FÓRMULA) ---
    private val _crearTemaUiState = MutableStateFlow(CrearTemaUiState())
    val crearTemaUiState = _crearTemaUiState.asStateFlow()

    fun onTituloChange(titulo: String) {
        _crearTemaUiState.update { it.copy(titulo = titulo, errorTitulo = null) }
    }

    fun onContenidoChange(contenido: String) {
        _crearTemaUiState.update { it.copy(contenido = contenido) }
    }

    fun seleccionarTema(id: Long) {
        _temaSeleccionado.value = ForoRepository.getTemaById(id)
    }

    fun guardarRol(rol: String) {
        viewModelScope.launch {
            userPreferencesRepository.saveUserRole(rol)
        }
    }

    // (Esta función la ignoraremos)
    fun validarYCrearTema(): Boolean {
        val estado = _crearTemaUiState.value
        if (estado.titulo.isBlank()) {
            _crearTemaUiState.update { it.copy(errorTitulo = "El título no puede estar vacío") }
            return false
        }
        val autor = Usuario(id = "2", nombre = "Nuevo Usuario", rol = rolUsuario.value ?: "registrado")
        ForoRepository.addTema(estado.titulo, estado.contenido, autor)
        _crearTemaUiState.value = CrearTemaUiState()
        return true
    }

    fun eliminarTema(tema: TemaForo) {
        ForoRepository.deleteTema(tema)
    }

    fun crearNuevoTema(titulo: String, contenido: String, categoria: String) {
        // Obtenemos el rol del usuario que está guardado
        val autor = Usuario(id = "2", nombre = "Nuevo Usuario", rol = rolUsuario.value ?: "registrado")

        // Llamamos al repositorio para añadir el tema
        ForoRepository.addTema(titulo, contenido, autor)

    }
    
}

