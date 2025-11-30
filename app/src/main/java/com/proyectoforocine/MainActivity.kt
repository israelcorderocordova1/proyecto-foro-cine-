package com.proyectoforocine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.proyectoforocine.model.TemaForo
import com.proyectoforocine.model.Usuario
import com.proyectoforocine.ui.theme.ProyectoForoCineTheme
import com.proyectoforocine.view.CrearTemaScreen
import com.proyectoforocine.view.DetalleTemaScreen
import com.proyectoforocine.view.ForgotPasswordScreen
import com.proyectoforocine.view.ListaTemasScreen
import com.proyectoforocine.view.LoginScreen
import com.proyectoforocine.view.MisTemasScreen
import com.proyectoforocine.view.PerfilScreen
import com.proyectoforocine.view.RegisterScreen
import com.proyectoforocine.viewmodel.AuthEvent
import com.proyectoforocine.viewmodel.AuthViewModel
import com.proyectoforocine.viewmodel.AuthViewModelFactory
import com.proyectoforocine.viewmodel.ForoViewModel
import com.proyectoforocine.viewmodel.ForoViewModelFactory
import com.proyectoforocine.viewmodel.PerfilViewModel
import com.proyectoforocine.viewmodel.PerfilViewModelFactory

class MainActivity : ComponentActivity() {

    private val foroViewModel: ForoViewModel by viewModels {
        ForoViewModelFactory((application as ForoApplication).repository)
    }
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(
            (application as ForoApplication).repository,
            (application as ForoApplication).sessionManager
        )
    }
    private val perfilViewModel: PerfilViewModel by viewModels {
        PerfilViewModelFactory((application as ForoApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val perfilUiState by perfilViewModel.uiState.collectAsState()
            val modoOscuro = perfilUiState.profile.modoOscuro

            ProyectoForoCineTheme(darkTheme = modoOscuro) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val isLoadingSession by authViewModel.isLoadingSession.collectAsState()
                    val currentUserId by authViewModel.currentUserId.collectAsState()

                    if (isLoadingSession) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    } else {
                        val startDestination = if (currentUserId != null) "lista_temas" else "login"

                        NavHost(
                            navController = navController,
                            startDestination = startDestination,
                            modifier = Modifier.testTag("navHostRoot")
                        ) {

                            composable("login") {
                                val loginState by authViewModel.loginUiState.collectAsState()
                                LoginScreen(
                                    uiState = loginState,
                                    onEmailChange = authViewModel::onLoginEmailChange,
                                    onPasswordChange = authViewModel::onLoginPasswordChange,
                                    onLoginClick = authViewModel::login,
                                    onNavigateToRegister = { navController.navigate("register") },
                                    onNavigateToForgotPassword = { navController.navigate("forgot_password") }
                                )
                            }

                            composable("register") {
                                val registerState by authViewModel.registerUiState.collectAsState()
                                RegisterScreen(
                                    uiState = registerState,
                                    onUsernameChange = authViewModel::onRegisterUsernameChange,
                                    onEmailChange = authViewModel::onRegisterEmailChange,
                                    onPasswordChange = authViewModel::onRegisterPasswordChange,
                                    onRegisterClick = authViewModel::register,
                                    onNavigateBack = { navController.popBackStack() }
                                )
                            }

                            composable("forgot_password") {
                                val forgotPasswordState by authViewModel.forgotPasswordUiState.collectAsState()
                                ForgotPasswordScreen(
                                    uiState = forgotPasswordState,
                                    onEmailChange = authViewModel::onForgotPasswordEmailChange,
                                    onRecoverClick = authViewModel::requestPasswordReset,
                                    onNavigateBack = { navController.popBackStack() }
                                )
                            }

                            composable("lista_temas") {
                                val temas by foroViewModel.temas.collectAsState()
                                ListaTemasScreen(
                                    temas = temas,
                                    onTemaClick = { tema -> navController.navigate("detalle_tema/${tema.id}") },
                                    onAddTemaClick = { navController.navigate("crear_tema") },
                                    onPerfilClick = { navController.navigate("perfil") },
                                    onLogoutClick = { 
                                        authViewModel.logout()
                                        navController.navigate("login") { popUpTo(0) }
                                    }
                                )
                            }

                            composable("mis_temas") {
                                val state by perfilViewModel.uiState.collectAsState()
                                MisTemasScreen(
                                    temas = state.temasUsuario,
                                    onNavigateBack = { navController.popBackStack() },
                                    onTemaClick = { tema -> navController.navigate("detalle_tema/${tema.id}") }
                                )
                            }

                            composable(
                                route = "detalle_tema/{temaId}",
                                arguments = listOf(navArgument("temaId") { type = NavType.IntType })
                            ) { backStackEntry ->
                                val temaId = backStackEntry.arguments?.getInt("temaId")
                                requireNotNull(temaId) { "El ID del tema no puede ser nulo" }

                                LaunchedEffect(temaId) {
                                    foroViewModel.seleccionarTema(temaId)
                                }

                                val tema by foroViewModel.temaSeleccionado.collectAsState()
                                val rol by foroViewModel.rolUsuario.collectAsState()

                                val temaForo = tema?.let {
                                    TemaForo(
                                        id = it.id.toLong(),
                                        titulo = it.titulo,
                                        contenido = it.contenido,
                                        autor = Usuario(id = "1", nombre = "Autor Anónimo", rol = "registrado"),
                                        categoria = "General"
                                    )
                                }

                                DetalleTemaScreen(
                                    tema = temaForo,
                                    rol = rol,
                                    onNavigateBack = { navController.popBackStack() },
                                    onDeleteTema = {
                                        tema?.let {
                                            foroViewModel.eliminarTema(it)
                                            navController.popBackStack()
                                        }
                                    }
                                )
                            }

                            composable("crear_tema") {
                                val uiState by foroViewModel.crearTemaUiState.collectAsState()
                                CrearTemaScreen(
                                    uiState = uiState,
                                    onTituloChange = foroViewModel::onTituloChange,
                                    onContenidoChange = foroViewModel::onContenidoChange,
                                    onPublicarClick = {
                                        currentUserId?.let {
                                            if (foroViewModel.validarYCrearTema(it)) {
                                                navController.popBackStack()
                                            }
                                        }
                                    },
                                    onNavigateBack = { navController.popBackStack() }
                                )
                            }

                            composable("perfil") {
                                currentUserId?.let { perfilViewModel.loadUserProfile(it) }
                                val state by perfilViewModel.uiState.collectAsState()

                                if (state.isLoading) {
                                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        CircularProgressIndicator()
                                    }
                                } else {
                                    PerfilScreen(
                                        uiState = state,
                                        onNavigateBack = { navController.popBackStack() },
                                        onMisTemasClick = { navController.navigate("mis_temas") },
                                        onFotoSeleccionada = perfilViewModel::onFotoSeleccionada,
                                        onModoOscuroToggle = perfilViewModel::onModoOscuroToggle,
                                        onNotificacionesToggle = perfilViewModel::onNotificacionesToggle,
                                        onShowImageSourceDialog = perfilViewModel::onShowImageSourceDialog,
                                        onHideImageSourceDialog = perfilViewModel::onHideImageSourceDialog
                                    )
                                }
                            }
                        }
                    }

                    val authEvent by authViewModel.authEvent.collectAsState()
                    LaunchedEffect(authEvent) {
                        when (val event = authEvent) {
                            is AuthEvent.Success -> {
                                foroViewModel.guardarRol(event.user.rol)
                                navController.navigate("lista_temas") { popUpTo("login") { inclusive = true } }
                                authViewModel.resetAuthEvent()
                            }
                            is AuthEvent.Error -> {
                                // Ya no es necesario mostrar el Toast aquí
                                authViewModel.resetAuthEvent()
                            }
                            else -> Unit
                        }
                    }
                }
            }
        }
    }
}