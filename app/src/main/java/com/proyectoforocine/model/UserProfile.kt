package com.proyectoforocine.model

/**
 * Modelo de datos para el perfil del usuario
 */
data class UserProfile(
    val nombre: String = "Usuario",
    val fotoUri: String? = null, // URI de la foto de perfil (null = usar avatar por defecto)
    val modoOscuro: Boolean = false,
    val recibirNotificaciones: Boolean = true
)

/**
 * Estado de la UI para la pantalla de perfil
 */
data class PerfilUiState(
    val profile: UserProfile = UserProfile(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showImageSourceDialog: Boolean = false // Para mostrar diálogo de selección (cámara/galería)
)
