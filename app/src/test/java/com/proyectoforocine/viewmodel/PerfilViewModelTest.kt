package com.proyectoforocine.viewmodel

import android.app.Application
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.proyectoforocine.data.UserPreferencesRepository
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

@OptIn(ExperimentalCoroutinesApi::class)
class PerfilViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var application: Application
    private lateinit var viewModel: PerfilViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        application = mockk(relaxed = true)

        // Simular constructor de UserPreferencesRepository
        mockkConstructor(UserPreferencesRepository::class)

        every { anyConstructed<UserPreferencesRepository>().userProfile } returns flowOf(
            UserProfile(
                nombre = "Usuario Test",
                fotoUri = null,
                modoOscuro = false,
                recibirNotificaciones = true
            )
        )

        coEvery { anyConstructed<UserPreferencesRepository>().saveUserName(any()) } just Runs
        coEvery { anyConstructed<UserPreferencesRepository>().saveUserPhoto(any()) } just Runs
        coEvery { anyConstructed<UserPreferencesRepository>().saveDarkMode(any()) } just Runs
        coEvery { anyConstructed<UserPreferencesRepository>().saveNotificationsEnabled(any()) } just Runs

        viewModel = PerfilViewModel(application)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `initial state should load from UserPreferencesRepository`() = runTest {
        // Dado - ViewModel inicializado
        testDispatcher.scheduler.advanceUntilIdle()

        // Entonces - userProfile cargado
        val profile = viewModel.userProfile.value
        assertEquals("Usuario Test", profile.nombre)
        assertNull(profile.fotoUri)
        assertFalse(profile.modoOscuro)
        assertTrue(profile.recibirNotificaciones)
    }

    @Test
    fun `onNombreChange should call repository saveUserName`() = runTest {
        // Dado
        val newName = "Nuevo Nombre"
        testDispatcher.scheduler.advanceUntilIdle()

        // Cuando
        viewModel.onNombreChange(newName)
        testDispatcher.scheduler.advanceUntilIdle()

        // Entonces
        coVerify { anyConstructed<UserPreferencesRepository>().saveUserName(newName) }
    }

    @Test
    fun `onFotoSeleccionada should call repository saveUserPhoto with URI`() = runTest {
        // Dado
        val uri = mockk<Uri>()
        every { uri.toString() } returns "content://test/photo.jpg"
        testDispatcher.scheduler.advanceUntilIdle()

        // Cuando
        viewModel.onFotoSeleccionada(uri)
        testDispatcher.scheduler.advanceUntilIdle()

        // Entonces
        coVerify { anyConstructed<UserPreferencesRepository>().saveUserPhoto("content://test/photo.jpg") }
    }

    @Test
    fun `onFotoSeleccionada should call repository saveUserPhoto with null`() = runTest {
        // Dado
        testDispatcher.scheduler.advanceUntilIdle()

        // Cuando
        viewModel.onFotoSeleccionada(null)
        testDispatcher.scheduler.advanceUntilIdle()

        // Entonces
        coVerify { anyConstructed<UserPreferencesRepository>().saveUserPhoto(null) }
    }

    @Test
    fun `onModoOscuroToggle should call repository saveDarkMode with true`() = runTest {
        // Dado
        testDispatcher.scheduler.advanceUntilIdle()

        // Cuando
        viewModel.onModoOscuroToggle(true)
        testDispatcher.scheduler.advanceUntilIdle()

        // Entonces
        coVerify { anyConstructed<UserPreferencesRepository>().saveDarkMode(true) }
    }

    @Test
    fun `onModoOscuroToggle should call repository saveDarkMode with false`() = runTest {
        // Dado
        testDispatcher.scheduler.advanceUntilIdle()

        // Cuando
        viewModel.onModoOscuroToggle(false)
        testDispatcher.scheduler.advanceUntilIdle()

        // Entonces
        coVerify { anyConstructed<UserPreferencesRepository>().saveDarkMode(false) }
    }

    @Test
    fun `onNotificacionesToggle should call repository saveNotificationsEnabled`() = runTest {
        // Dado
        testDispatcher.scheduler.advanceUntilIdle()

        // Cuando
        viewModel.onNotificacionesToggle(false)
        testDispatcher.scheduler.advanceUntilIdle()

        // Entonces
        coVerify { anyConstructed<UserPreferencesRepository>().saveNotificationsEnabled(false) }
    }

    @Test
    fun `onShowImageSourceDialog should set showImageSourceDialog to true`() = runTest {
        // Given
        testDispatcher.scheduler.advanceUntilIdle()
        assertFalse(viewModel.uiState.value.showImageSourceDialog)

        // When
        viewModel.onShowImageSourceDialog()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value.showImageSourceDialog)
    }

    @Test
    fun `onHideImageSourceDialog should set showImageSourceDialog to false`() = runTest {
        // Given
        viewModel.onShowImageSourceDialog()
        testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(viewModel.uiState.value.showImageSourceDialog)

        // When
        viewModel.onHideImageSourceDialog()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertFalse(viewModel.uiState.value.showImageSourceDialog)
    }
}
