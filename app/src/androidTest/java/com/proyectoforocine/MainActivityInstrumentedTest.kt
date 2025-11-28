package com.proyectoforocine

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

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
            assertTrue("La aplicación debe ser ForoApplication",
                application is ForoApplication)
            
            // Verificar que ForoApplication tiene database
            val foroApp = application as ForoApplication
            assertNotNull("Database debe estar inicializado", foroApp.database)
            assertNotNull("Repository debe estar inicializado", foroApp.repository)
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
