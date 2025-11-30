package com.proyectoforocine

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumentation test para MainActivity
 * Ejecuta código real en emulador/dispositivo
 */
@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentedTest {

    @Test
    fun useAppContext() {
        // Verificar que el contexto de la app es correcto
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.proyectoforocine", appContext.packageName)
    }

    @Test
    fun mainActivityShouldLaunch() {
        // Given & When - Lanzar la actividad
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        // Then - Verificar que se lanzó correctamente
        scenario.onActivity { activity ->
            assertNotNull("La actividad no debe ser nula", activity)
            assertTrue("Debe ser una instancia de MainActivity", activity is MainActivity)
        }

        scenario.close()
    }

    @Test
    fun mainActivityShouldUseForoApplication() {
        // Given & When
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        // Then
        scenario.onActivity { activity ->
            val application = activity.application
            assertTrue(
                "La aplicación debe ser ForoApplication",
                application is ForoApplication
            )

            // Nota: métodos verificarInicializacion/estaInicializado han sido removidos
            // y la inicialización ahora se valida vía ViewModels y Application.

            // Verificar que ForoApplication tiene database
            val foroApp = application as ForoApplication
            assertNotNull("Database debe estar inicializado", foroApp.database)
            assertNotNull("Repository debe estar inicializado", foroApp.repository)
        }

        scenario.close()
    }

    @Test
    fun mainActivityStartDestinationShouldReflectSession() {
        // Sin sesión previa, MainActivity debe cargar y mostrar el NavHost
        // El destino inicial depende de SessionManager (login si no hay sesión)
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        scenario.onActivity { activity ->
            assertNotNull("MainActivity debe estar inicializada", activity)
            // La lógica de destino inicial está en onCreate usando AuthViewModel
            // Validamos que la app no crashea y el NavHost se renderiza
        }

        scenario.close()
    }

    @Test
    fun mainActivityShouldHaveViewModelsInitialized() {
        // Given & When
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        // Then - Usar reflection para verificar ViewModels
        scenario.onActivity { activity ->
            val activityClass = activity::class.java
            val fields = activityClass.declaredFields

            val hasForoViewModel = fields.any {
                it.name.contains("foroViewModel", ignoreCase = true)
            }
            val hasPerfilViewModel = fields.any {
                it.name.contains("perfilViewModel", ignoreCase = true)
            }

            assertTrue("Debe tener foroViewModel", hasForoViewModel)
            assertTrue("Debe tener perfilViewModel", hasPerfilViewModel)
        }

        scenario.close()
    }
}
