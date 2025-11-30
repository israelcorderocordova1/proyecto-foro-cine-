package com.proyectoforocine.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyectoforocine.data.ForoRepository
import com.proyectoforocine.data.datastore.SessionManager
import com.proyectoforocine.data.local.UsuarioEntity
import com.proyectoforocine.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

// --- Estados de la UI ---

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val loginError: String? = null
)

data class RegisterUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val usernameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val registrationError: String? = null
)

data class ForgotPasswordUiState(
    val email: String = "",
    val feedbackMessage: String? = null, // Mensaje unificado para éxito y error
    val isError: Boolean = false // Para colorear el texto
)

// --- Eventos de Autenticación ---

sealed class AuthEvent {
    data object Loading : AuthEvent()
    data class Success(val user: Usuario) : AuthEvent()
    data class Error(val message: String) : AuthEvent()
    data object Idle : AuthEvent()
}

class AuthViewModel(
    private val repository: ForoRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    // --- Estados de la UI ---
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    private val _registerUiState = MutableStateFlow(RegisterUiState())
    val registerUiState: StateFlow<RegisterUiState> = _registerUiState.asStateFlow()

    private val _forgotPasswordUiState = MutableStateFlow(ForgotPasswordUiState())
    val forgotPasswordUiState: StateFlow<ForgotPasswordUiState> = _forgotPasswordUiState.asStateFlow()

    // --- Eventos de Autenticación ---
    private val _authEvent = MutableStateFlow<AuthEvent>(AuthEvent.Idle)
    val authEvent: StateFlow<AuthEvent> = _authEvent.asStateFlow()

    // --- Estado de la Sesión ---
    private val _isLoadingSession = MutableStateFlow(true)
    val isLoadingSession: StateFlow<Boolean> = _isLoadingSession.asStateFlow()

    val currentUserId: StateFlow<Long?> = sessionManager.currentUserIdFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        viewModelScope.launch {
            sessionManager.currentUserIdFlow.first()
            _isLoadingSession.value = false
        }
    }

    // --- Manejadores de cambios ---

    fun onLoginEmailChange(email: String) {
        _loginUiState.update { it.copy(email = email, emailError = if (isValidEmail(email)) null else "Email inválido", loginError = null) }
    }

    fun onLoginPasswordChange(password: String) {
        _loginUiState.update { it.copy(password = password, loginError = null) }
    }

    fun onRegisterUsernameChange(username: String) {
        _registerUiState.update { it.copy(username = username) }
    }

    fun onRegisterEmailChange(email: String) {
        _registerUiState.update { it.copy(email = email, emailError = if (isValidEmail(email)) null else "Email inválido") }
    }

    fun onRegisterPasswordChange(password: String) {
        _registerUiState.update { it.copy(password = password, passwordError = if (password.length >= 6) null else "Mínimo 6 caracteres") }
    }

    fun onForgotPasswordEmailChange(email: String) {
        _forgotPasswordUiState.update { it.copy(email = email, feedbackMessage = null) }
    }

    // --- Lógica de Negocio ---

    fun requestPasswordReset() {
        val state = _forgotPasswordUiState.value
        if (!isValidEmail(state.email)) {
            _forgotPasswordUiState.update { it.copy(feedbackMessage = "Formato de correo inválido", isError = true) }
            return
        }

        viewModelScope.launch {
            val user = repository.getUsuarioPorEmail(state.email)
            if (user == null) {
                _forgotPasswordUiState.update { it.copy(feedbackMessage = "Correo no existe", isError = true) }
            } else {
                _forgotPasswordUiState.update { it.copy(feedbackMessage = "Revisa tu correo para recuperar la contraseña", isError = false) }
            }
        }
    }

    fun login() {
        val state = _loginUiState.value
        if (!isValidEmail(state.email) || state.password.isBlank()) {
            _loginUiState.update { it.copy(loginError = "Email o contraseña inválidos") }
            return
        }

        viewModelScope.launch {
            _authEvent.value = AuthEvent.Loading
            val userEntity = repository.getUsuarioPorEmail(state.email)
            if (userEntity != null && userEntity.password == state.password) {
                sessionManager.saveUserId(userEntity.id)
                val userModel = userEntity.toUserModel()
                _authEvent.value = AuthEvent.Success(userModel)
            } else {
                _loginUiState.update { it.copy(loginError = "Credenciales incorrectas") } // Cambio clave aquí
                _authEvent.value = AuthEvent.Idle // Reseteamos el evento
            }
        }
    }

    fun register() {
        val state = _registerUiState.value
        if (state.username.isBlank() || !isValidEmail(state.email) || state.password.length < 6) {
            _registerUiState.update { it.copy(registrationError = "Revisa los campos") }
            return
        }

        viewModelScope.launch {
            _authEvent.value = AuthEvent.Loading
            val existingUser = repository.getUsuarioPorEmail(state.email)
            if (existingUser != null) {
                _authEvent.value = AuthEvent.Error("El correo ya está en uso")
                return@launch
            }

            val newUserEntity = UsuarioEntity(
                username = state.username,
                email = state.email,
                password = state.password,
                rol = "registrado",
                registrationDate = Date().time
            )
            val newId = repository.insertarUsuario(newUserEntity)
            if (newId != -1L) {
                sessionManager.saveUserId(newId)
                val userModel = newUserEntity.copy(id = newId).toUserModel()
                _authEvent.value = AuthEvent.Success(userModel)
            } else {
                _authEvent.value = AuthEvent.Error("No se pudo completar el registro")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession()
        }
    }

    fun resetAuthEvent() {
        _authEvent.value = AuthEvent.Idle
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

fun UsuarioEntity.toUserModel(): Usuario {
    return Usuario(
        id = this.id.toString(),
        nombre = this.username,
        rol = this.rol
    )
}
