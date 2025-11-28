package com.proyectoforocine

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

/**
 * Instrumentation test para ForoApplication
 * Ejecuta código real y verifica inicialización de database y repository
 */
@RunWith(AndroidJUnit4::class)
class ForoApplicationInstrumentedTest {

    @Test
    fun foroApplicationShouldInitializeDatabase() {
        // Given
        val app = ApplicationProvider.getApplicationContext<ForoApplication>()

        // When
        val database = app.database

        // Then
        assertNotNull("Database no debe ser nulo", database)
        assertTrue(
            "Debe ser una instancia de AppDatabase",
            database is com.proyectoforocine.data.local.AppDatabase
        )
        // Room database puede no estar "open" hasta que se use
    }

    @Test
    fun foroApplicationShouldInitializeRepository() {
        // Given
        val app = ApplicationProvider.getApplicationContext<ForoApplication>()

        // When
        val repository = app.repository

        // Then
        assertNotNull("Repository no debe ser nulo", repository)
        assertTrue(
            "Debe ser una instancia de ForoRepository",
            repository is com.proyectoforocine.data.ForoRepository
        )
    }

    @Test
    fun databaseAndRepositoryShouldUseLazyInitialization() {
        // Given
        val app = ApplicationProvider.getApplicationContext<ForoApplication>()

        // When - Acceder múltiples veces
        val db1 = app.database
        val db2 = app.database
        val repo1 = app.repository
        val repo2 = app.repository

        // Then - Deben ser la misma instancia (singleton por lazy)
        assertSame("Database debe ser la misma instancia", db1, db2)
        assertSame("Repository debe ser la misma instancia", repo1, repo2)
    }

    @Test
    fun repositoryShouldHaveAccessToDao() {
        // Given
        val app = ApplicationProvider.getApplicationContext<ForoApplication>()

        // When
        val database = app.database
        val dao = database.temaDao()

        // Then
        assertNotNull("TemaDao no debe ser nulo", dao)
    }
}
