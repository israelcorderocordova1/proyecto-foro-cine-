package com.proyectoforocine.viewmodel

// Pega esto en tu archivo CrearTemaScreen.kt o en un nuevo archivo de ViewModel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.proyectoforocine.data.local.Tema
import com.proyectoforocine.data.local.TemaRepository
import kotlinx.coroutines.launch

class CrearTemaViewModel(private val repository: TemaRepository) : ViewModel() {

    // Función para insertar un tema. Se llama desde la UI.
    fun insertarTema(titulo: String, contenido: String) {
        // Usa viewModelScope para lanzar una corrutina de forma segura.
        viewModelScope.launch {
            val nuevoTema = Tema(titulo = titulo, contenido = contenido)
            repository.insertarTema(nuevoTema)
        }
    }
}

/**
 * Fábrica para crear el CrearTemaViewModel con el repositorio como dependencia.
 */
class CrearTemaViewModelFactory(private val repository: TemaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CrearTemaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // --- INICIO DE LA CORRECCIÓN ---
            // Se corrigió el nombre de la clase de 'CreararTemaViewModel' a 'CrearTemaViewModel'
            return CrearTemaViewModel(repository) as T
            // --- FIN DE LA CORRECCIÓN ---
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
