package com.proyectoforocine.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.proyectoforocine.data.ForoRepository
import com.proyectoforocine.data.datastore.SessionManager

/**
 * Factory para crear una instancia de AuthViewModel con sus dependencias:
 * el repositorio y el gestor de sesión.
 */
class AuthViewModelFactory(
    private val repository: ForoRepository,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}