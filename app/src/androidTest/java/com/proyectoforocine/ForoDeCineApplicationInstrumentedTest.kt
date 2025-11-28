package com.proyectoforocine

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

/**
 * Instrumentation test para ForoDeCineApplication
 * NOTA: Esta clase existe en el código pero la app usa ForoApplication en producción
 */
@RunWith(AndroidJUnit4::class)
class ForoDeCineApplicationInstrumentedTest {

    @Test
    fun foroDeCineApplicationClassExists() {
        // Verificar que la clase existe (aunque no se use en producción)
        val clazz = ForoDeCineApplication::class.java
        assertNotNull("ForoDeCineApplication class debe existir", clazz)
    }

    @Test
    fun foroDeCineApplicationExtendsApplication() {
        // Verificar que hereda de Application
        val clazz = ForoDeCineApplication::class.java
        assertTrue(
            "Debe heredar de Application",
            android.app.Application::class.java.isAssignableFrom(clazz)
        )
    }

    @Test
    fun foroDeCineApplicationHasDatabaseProperty() {
        // Verificar que tiene propiedad database
        val fields = ForoDeCineApplication::class.java.declaredFields
        val hasDatabaseField = fields.any { it.name.contains("database", ignoreCase = true) }
        assertTrue("Debe tener propiedad database", hasDatabaseField)
    }

    @Test
    fun foroDeCineApplicationHasRepositoryProperty() {
        // Verificar que tiene propiedad repository
        val fields = ForoDeCineApplication::class.java.declaredFields
        val hasRepositoryField = fields.any { it.name.contains("repository", ignoreCase = true) }
        assertTrue("Debe tener propiedad repository", hasRepositoryField)
    }
}
