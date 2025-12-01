package com.proyectoforocine.viewmodel // <- AÑADIDO: El paquete es importante

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.proyectoforocine.data.ForoRepository

// --- CORRECCIÓN CLAVE ---
// Importa la clase ForoViewModel para que pueda ser utilizada en este archivo.
import com.proyectoforocine.viewmodel.ForoViewModel

/**
 * Factory (fábrica) para crear instancias de ForoViewModel.
 *
 * Esta clase es necesaria porque nuestro ForoViewModel tiene un constructor
 * que requiere una dependencia (el ForoRepository), y no podemos dejar
 * que el sistema lo cree automáticamente. La Factory se encarga de recibir
 * esa dependencia y pasársela al ViewModel cuando se crea.
 */
class ForoViewModelFactory(private val repository: ForoRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Comprueba si la clase de ViewModel que el sistema nos pide es ForoViewModel
        if (modelClass.isAssignableFrom(ForoViewModel::class.java)) {
            // Si lo es, creamos una instancia de ForoViewModel, pasándole el repositorio,
            // y la devolvemos. La supresión de la advertencia es segura por la comprobación anterior.
            @Suppress("UNCHECKED_CAST")
            return ForoViewModel(repository) as T
        }
        // Si el sistema intenta usar esta factory para crear cualquier otro tipo de ViewModel,
        // lanzamos una excepción para indicar que algo está mal.
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
