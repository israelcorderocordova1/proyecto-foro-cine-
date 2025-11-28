package com.proyectoforocine

import android.os.Bundle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pruebas unitarias para MainActivity
 * Verifican la estructura y configuración de la clase
 */
class MainActivityTest {

    @Test
    fun `MainActivity debe heredar de ComponentActivity`() {
        // Dado
        val activityClass = MainActivity::class.java

        // Cuando y Entonces
        assertTrue(
            "MainActivity debe heredar de ComponentActivity",
            androidx.activity.ComponentActivity::class.java.isAssignableFrom(activityClass)
        )
    }

    @Test
    fun `MainActivity debe tener metodo onCreate`() {
        // Dado
        val activityClass = MainActivity::class.java

        // Cuando
        val onCreateMethod = activityClass.getDeclaredMethod("onCreate", Bundle::class.java)

        // Entonces
        assertNotNull("El método onCreate debe existir", onCreateMethod)

        // Verificar que el método es protected o public (estándar de Android)
        val modifiers = onCreateMethod.modifiers
        assertTrue(
            "onCreate debe ser accesible",
            modifiers and java.lang.reflect.Modifier.PUBLIC != 0 ||
                    modifiers and java.lang.reflect.Modifier.PROTECTED != 0
        )
    }

    @Test
    fun `MainActivity debe tener ViewModels inicializados`() {
        // Dado
        val activityClass = MainActivity::class.java
        val fields = activityClass.declaredFields

        // Cuando
        val hasForoViewModel = fields.any { field ->
            field.name.contains("foroViewModel", ignoreCase = true)
        }
        val hasPerfilViewModel = fields.any { field ->
            field.name.contains("perfilViewModel", ignoreCase = true)
        }

        // Entonces
        assertTrue("MainActivity debe tener un campo foroViewModel", hasForoViewModel)
        assertTrue("MainActivity debe tener un campo perfilViewModel", hasPerfilViewModel)
    }

    @Test
    fun `MainActivity debe tener constructor sin parametros`() {
        // Dado
        val activityClass = MainActivity::class.java

        // When
        val constructors = activityClass.declaredConstructors
        val hasNoArgConstructor = constructors.any { it.parameterCount == 0 }

        // Then
        assertTrue("Debe tener un constructor sin parámetros", hasNoArgConstructor)
    }

    @Test
    fun `MainActivity debe estar en el package correcto`() {
        // Given
        val activityClass = MainActivity::class.java

        // When
        val packageName = activityClass.`package`?.name

        // Then
        assertEquals(
            "Debe estar en el package com.proyectoforocine",
            "com.proyectoforocine", packageName
        )
    }

    @Test
    fun `MainActivity debe usar ForoViewModel`() {
        // Given
        val activityClass = MainActivity::class.java
        val fields = activityClass.declaredFields

        // Cuando - Verificar que tiene ForoViewModel
        val usesForoViewModel = fields.any {
            it.type.name.contains("ForoViewModel") ||
                    it.name.contains("foroViewModel", ignoreCase = true)
        }

        // Entonces
        assertTrue("Debe usar ForoViewModel", usesForoViewModel)
    }
}
