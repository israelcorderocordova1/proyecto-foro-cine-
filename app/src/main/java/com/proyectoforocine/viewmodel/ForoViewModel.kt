package com.proyectoforocine.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyectoforocine.data.ForoRepository
import com.proyectoforocine.data.local.Tema
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Define el estado de la UI para la pantalla de creación
data class CrearTemaUiState(
    val titulo: String = "",
    val contenido: String = "",
    val error: String? = null
)

// --- CORRECCIÓN CLAVE ---
// El constructor ahora espera un 'ForoRepository', que coincide con lo que la Factory le pasa.
class ForoViewModel(private val repository: ForoRepository) : ViewModel() {

    // --- Estado para el rol del usuario ---
    private val _rolUsuario = MutableStateFlow<String?>("registrado")
    val rolUsuario: StateFlow<String?> = _rolUsuario.asStateFlow()

    // --- Estado para la lista de temas ---
    val temas: StateFlow<List<Tema>> = repository.todosLosTemas
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // --- Estado para un tema seleccionado ---
    private val _temaSeleccionado = MutableStateFlow<Tema?>(null)
    val temaSeleccionado: StateFlow<Tema?> = _temaSeleccionado.asStateFlow()
    // --- Estado para la pantalla de creación ---
    private val _crearTemaUiState = MutableStateFlow(CrearTemaUiState())
    val crearTemaUiState: StateFlow<CrearTemaUiState> = _crearTemaUiState.asStateFlow()

    fun guardarRol(rol: String) {
        _rolUsuario.value = rol
    }

    fun seleccionarTema(id: Int) {
        viewModelScope.launch {
            repository.obtenerTemaPorId(id.toLong()).collect { tema ->
                _temaSeleccionado.value = tema
            }
        }
    }
    fun eliminarTema(tema: Tema) {
        viewModelScope.launch {
            repository.eliminarTema(tema)
        }
    }

    // --- Lógica para CrearTemaScreen ---
    fun onTituloChange(nuevoTitulo: String) {
        _crearTemaUiState.update { it.copy(titulo = nuevoTitulo, error = null) }
    }

    fun onContenidoChange(nuevoContenido: String) {
        _crearTemaUiState.update { it.copy(contenido = nuevoContenido, error = null) }
    }
    fun validarYCrearTema(): Boolean {
        val titulo = _crearTemaUiState.value.titulo
        val contenido = _crearTemaUiState.value.contenido

        if (titulo.isBlank() || contenido.isBlank()) {
            _crearTemaUiState.update { it.copy(error = "El título y el contenido no pueden estar vacíos") }
            return false
        }
        viewModelScope.launch {
            val nuevoTema = Tema(titulo = titulo, contenido = contenido)
            repository.insertarTema(nuevoTema)
        }

        // Limpiar estado después de crear
        _crearTemaUiState.value = CrearTemaUiState()
        return true
    }
}
