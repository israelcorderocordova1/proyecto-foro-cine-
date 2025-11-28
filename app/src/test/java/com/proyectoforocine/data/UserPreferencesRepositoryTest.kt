package com.proyectoforocine.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.proyectoforocine.model.UserProfile
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

/**
 * Pruebas unitarias simplificadas para UserPreferencesRepository
 * Nota: Algunos tests requieren instrumentación de Android debido a la complejidad de DataStore
 */
@OptIn(ExperimentalCoroutinesApi::class)
class UserPreferencesRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var context: Context
    private lateinit var repository: UserPreferencesRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun configuracion() {
        Dispatchers.setMain(testDispatcher)
        context = mockk(relaxed = true)

        // Nota: UserPreferencesRepository requiere DataStore real para tests completos
        // Estos tests verifican la estructura de la clase
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `el repositorio debe tener las claves de preferencias definidas`() {
        // Verificar que las claves estáticas existen
        assertNotNull("USER_ROLE_KEY debe existir", UserPreferencesRepository.USER_ROLE_KEY)
        assertNotNull("USER_NAME_KEY debe existir", UserPreferencesRepository.USER_NAME_KEY)
        assertNotNull(
            "USER_PHOTO_URI_KEY debe existir",
            UserPreferencesRepository.USER_PHOTO_URI_KEY
        )
        assertNotNull("DARK_MODE_KEY debe existir", UserPreferencesRepository.DARK_MODE_KEY)
        assertNotNull(
            "NOTIFICATIONS_ENABLED_KEY debe existir",
            UserPreferencesRepository.NOTIFICATIONS_ENABLED_KEY
        )
    }

    @Test
    fun `las claves de preferencias deben tener nombres correctos`() {
        // Verificar que los nombres de las claves son los esperados
        assertEquals("user_role", UserPreferencesRepository.USER_ROLE_KEY.name)
        assertEquals("user_name", UserPreferencesRepository.USER_NAME_KEY.name)
        assertEquals("user_photo_uri", UserPreferencesRepository.USER_PHOTO_URI_KEY.name)
        assertEquals("dark_mode", UserPreferencesRepository.DARK_MODE_KEY.name)
        assertEquals(
            "notifications_enabled",
            UserPreferencesRepository.NOTIFICATIONS_ENABLED_KEY.name
        )
    }

    @Test
    fun `el repositorio debe poder instanciarse con un Context`() {
        // Verificar que se puede crear una instancia del repositorio
        val testRepository = UserPreferencesRepository(context)
        assertNotNull("El repositorio no debe ser nulo", testRepository)
    }

    @Test
    fun `la clase debe tener todos los métodos públicos esperados`() {
        // Verificar que los métodos existen mediante reflection
        val repositoryClass = UserPreferencesRepository::class.java
        val methods = repositoryClass.declaredMethods.map { it.name }

        assertTrue("Debe tener saveUserRole", methods.contains("saveUserRole"))
        assertTrue("Debe tener saveUserName", methods.contains("saveUserName"))
        assertTrue("Debe tener saveUserPhoto", methods.contains("saveUserPhoto"))
        assertTrue("Debe tener saveDarkMode", methods.contains("saveDarkMode"))
        assertTrue(
            "Debe tener saveNotificationsEnabled",
            methods.contains("saveNotificationsEnabled")
        )
        assertTrue("Debe tener saveUserProfile", methods.contains("saveUserProfile"))
    }

    @Test
    fun `el repositorio debe tener flows para userProfile y userRole`() {
        // Verificar que los campos de Flow existen
        val repositoryClass = UserPreferencesRepository::class.java
        val fields = repositoryClass.declaredFields.map { it.name }

        assertTrue("Debe tener userProfile flow", fields.contains("userProfile"))
        assertTrue("Debe tener userRole flow", fields.contains("userRole"))
    }
}
