package com.proyectoforocine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.proyectoforocine.ui.theme.ProyectoForoCineTheme
import com.proyectoforocine.view.CrearTemaScreen
import com.proyectoforocine.view.DetalleTemaScreen
import com.proyectoforocine.view.ListaTemasScreen
import com.proyectoforocine.view.LoginScreen
import com.proyectoforocine.view.PerfilScreen
import com.proyectoforocine.viewmodel.ForoViewModel
import com.proyectoforocine.viewmodel.PerfilViewModel

class MainActivity : ComponentActivity() {

    private val foroViewModel: ForoViewModel by viewModels()
    private val perfilViewModel: PerfilViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Observar modo oscuro desde el perfil
            val perfilUiState by perfilViewModel.uiState.collectAsState()
            val modoOscuro = perfilUiState.profile.modoOscuro
            
            ProyectoForoCineTheme(darkTheme = modoOscuro) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "login") {

                        composable("login") {
                            LoginScreen(
                                onLoginAsUser = {
                                    foroViewModel.guardarRol("registrado")
                                    navController.navigate("lista_temas") { popUpTo("login") { inclusive = true } }
                                },
                                onLoginAsModerator = {
                                    foroViewModel.guardarRol("moderador")
                                    navController.navigate("lista_temas") { popUpTo("login") { inclusive = true } }
                                }
                            )
                        }

                        composable("lista_temas") {
                            ListaTemasScreen(
                                temas = foroViewModel.temas,
                                onTemaClick = { tema -> navController.navigate("detalle_tema/${tema.id}") },
                                onAddTemaClick = { navController.navigate("crear_tema") },
                                onPerfilClick = { navController.navigate("perfil") }
                            )
                        }

                        composable(
                            route = "detalle_tema/{temaId}",
                            arguments = listOf(navArgument("temaId") { type = NavType.LongType })
                        ) { backStackEntry ->
                            val temaId = backStackEntry.arguments?.getLong("temaId")
                            requireNotNull(temaId) { "El ID del tema no puede ser nulo" }

                            LaunchedEffect(temaId) {
                                foroViewModel.seleccionarTema(temaId)
                            }

                            val tema by foroViewModel.temaSeleccionado.collectAsState()
                            val rol by foroViewModel.rolUsuario.collectAsState()

                            DetalleTemaScreen(
                                tema = tema,
                                rol = rol,
                                onNavigateBack = { navController.popBackStack() },
                                onDeleteTema = {
                                    tema?.let { foroViewModel.eliminarTema(it) }
                                    navController.popBackStack()
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
                                    if (foroViewModel.validarYCrearTema()) {
                                        navController.popBackStack()
                                    }
                                },
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        composable("perfil") {
                            val uiState by perfilViewModel.uiState.collectAsState()
                            PerfilScreen(
                                uiState = uiState,
                                onNombreChange = perfilViewModel::onNombreChange,
                                onFotoSeleccionada = perfilViewModel::onFotoSeleccionada,
                                onModoOscuroToggle = perfilViewModel::onModoOscuroToggle,
                                onNotificacionesToggle = perfilViewModel::onNotificacionesToggle,
                                onShowImageSourceDialog = perfilViewModel::onShowImageSourceDialog,
                                onHideImageSourceDialog = perfilViewModel::onHideImageSourceDialog,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}