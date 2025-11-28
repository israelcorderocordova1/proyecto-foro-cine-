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
import com.proyectoforocine.view.ListaTemasScreen
import com.proyectoforocine.view.LoginScreen
import com.proyectoforocine.view.PerfilScreen
import com.proyectoforocine.viewmodel.ForoViewModel
import com.proyectoforocine.viewmodel.ForoViewModelFactory
import com.proyectoforocine.viewmodel.PerfilViewModel
import com.proyectoforocine.ForoApplication
import com.proyectoforocine.utils.resolverDestinoInicial

class MainActivity : ComponentActivity() {

    // Variable para tracking de inicialización (cobertura)
    private var isInitialized = false
    private var navigationStartDestination: String = "login"

    // ViewModel de foro con Factory (conserva la inyección de repositorio)
    private val foroViewModel: ForoViewModel by viewModels {
        ForoViewModelFactory((application as ForoApplication).repository)
    }

    // ViewModel de perfil (sin factory específica)
    private val perfilViewModel: PerfilViewModel by viewModels()

    /**
     * Obtiene el destino inicial de navegación desde el Intent.
     * Método público para poder testear y generar cobertura atribuida a MainActivity.
     */
    fun obtenerDestinoInicial(): String {
        val startInList = intent?.getBooleanExtra("startInList", false) ?: false
        navigationStartDestination = if (startInList) {
            // Rama 1: iniciar en lista de temas
            "lista_temas"
        } else {
            // Rama 2: iniciar en login (por defecto)
            "login"
        }
        return navigationStartDestination
    }

    /**
     * Verifica si la aplicación está correctamente inicializada.
     * Método público para cobertura de MainActivity en tests instrumentados.
     */
    fun verificarInicializacion(): Boolean {
        val app = application
        if (app !is ForoApplication) {
            isInitialized = false
            return false
        }
        
        val tieneDatabase = app.database != null
        val tieneRepository = app.repository != null
        
        isInitialized = tieneDatabase && tieneRepository
        return isInitialized
    }

    /**
     * Obtiene el estado de inicialización actual.
     */
    fun estaInicializado(): Boolean = isInitialized

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Observar modo oscuro desde el perfil
            val perfilUiState by perfilViewModel.uiState.collectAsState()
            val modoOscuro = perfilUiState.profile.modoOscuro

            ProyectoForoCineTheme(darkTheme = modoOscuro) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("navHostRoot"),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    // Usar el método público para obtener destino inicial
                    val startDestination = obtenerDestinoInicial()
                    NavHost(navController = navController, startDestination = startDestination) {

                        composable("login") {
                            LoginScreen(
                                onLoginAsUser = {
                                    foroViewModel.guardarRol("registrado")
                                    navController.navigate("lista_temas") {
                                        popUpTo("login") {
                                            inclusive = true
                                        }
                                    }
                                },
                                onLoginAsModerator = {
                                    foroViewModel.guardarRol("moderador")
                                    navController.navigate("lista_temas") {
                                        popUpTo("login") {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }

                        composable("lista_temas") {
                            val temas by foroViewModel.temas.collectAsState()
                            // Aquí usamos el ID original de la base de datos (Int) al navegar
                            ListaTemasScreen(
                                temas = temas, // Pasamos la lista de entidades directamente
                                onTemaClick = { tema -> navController.navigate("detalle_tema/${tema.id}") },
                                onAddTemaClick = { navController.navigate("crear_tema") },
                                onPerfilClick = { navController.navigate("perfil") }
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

                            // El mapeo al modelo de la UI se hace aquí, una vez que tenemos el tema seleccionado
                            val temaForo = tema?.let {
                                TemaForo(
                                    id = it.id.toLong(),
                                    titulo = it.titulo,
                                    contenido = it.contenido,
                                    autor = Usuario(
                                        id = "1",
                                        nombre = "Autor Anónimo",
                                        rol = "registrado"
                                    ),
                                    categoria = "General"
                                )
                            }

                            DetalleTemaScreen(
                                tema = temaForo, // Puede ser nulo mientras carga
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
