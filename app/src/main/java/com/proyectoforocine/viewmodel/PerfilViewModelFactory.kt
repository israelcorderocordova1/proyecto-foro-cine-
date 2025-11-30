package com.proyectoforocine.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.proyectoforocine.data.ForoRepository

class PerfilViewModelFactory(private val repository: ForoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerfilViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PerfilViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}