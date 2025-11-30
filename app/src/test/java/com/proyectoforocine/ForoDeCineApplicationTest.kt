package com.proyectoforocine

import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pruebas unitarias para ForoDeCineApplication
 * Verifican la estructura y configuración de la clase
 */
class ForoDeCineApplicationTest {

    @Test
    fun `ForoDeCineApplication debe heredar de Application`() {
        // Dado
        val appClass = ForoDeCineApplication::class.java

        // Cuando y Entonces
        assertTrue(
            "ForoDeCineApplication debe heredar de Application",
            android.app.Application::class.java.isAssignableFrom(appClass)
        )
    }

    @Test
    fun `ForoDeCineApplication debe tener propiedad database`() {
        // Dado
        val appClass = ForoDeCineApplication::class.java
        val fields = appClass.declaredFields

        // Cuando
        val hasDatabaseField = fields.any { field ->
            field.name.contains("database", ignoreCase = true)
        }

        // Entonces
        assertTrue("Debe tener campo database", hasDatabaseField)
    }

    @Test
    fun `ForoDeCineApplication debe tener propiedad repository`() {
        // Dado
        val appClass = ForoDeCineApplication::class.java
        val fields = appClass.declaredFields

        // Cuando
        val hasRepositoryField = fields.any { field ->
            field.name.contains("repository", ignoreCase = true)
        }

        // Entonces
        assertTrue("Debe tener campo repository", hasRepositoryField)
    }

    @Test
    fun `database y repository deben usar lazy delegation`() {
        // Dado
        val appClass = ForoDeCineApplication::class.java

        // Cuando - Verificar que hay propiedades Lazy
        val fields = appClass.declaredFields
        val lazyFields = fields.filter { it.type.name.contains("Lazy") }

        // Entonces
        assertTrue("Debe tener al menos una propiedad lazy", lazyFields.isNotEmpty())
    }

    @Test
    fun `ForoDeCineApplication debe tener constructor sin parametros`() {
        // Dado
        val appClass = ForoDeCineApplication::class.java

        // Cuando
        val constructors = appClass.declaredConstructors
        val hasNoArgConstructor = constructors.any { it.parameterCount == 0 }

        // Entonces
        assertTrue("Debe tener un constructor sin parámetros", hasNoArgConstructor)
    }
}
