package com.proyectoforocine.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.proyectoforocine.data.ForoRepository
import com.proyectoforocine.data.local.Tema
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ForoViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: ForoRepository
    private lateinit var viewModel: ForoViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)

        // Simular flujos del repositorio
        every { repository.todosLosTemas } returns flowOf(emptyList())

        viewModel = ForoViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `initial state should have default values`() = runTest {
        // Dado - ViewModel inicializado

        // Cuando - Colección de estados iniciales
        testDispatcher.scheduler.advanceUntilIdle()

        // Entonces - Los valores iniciales deben ser correctos
        assertNull(viewModel.temaSeleccionado.value)
        assertEquals("registrado", viewModel.rolUsuario.value)
        assertEquals("", viewModel.crearTemaUiState.value.titulo)
        assertEquals("", viewModel.crearTemaUiState.value.contenido)
        assertNull(viewModel.crearTemaUiState.value.error)
    }

    @Test
    fun `guardarRol should update rolUsuario state`() = runTest {
        // Given
        val newRole = "moderador"

        // When
        viewModel.guardarRol(newRole)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(newRole, viewModel.rolUsuario.value)
    }

    @Test
    fun `onTituloChange should update titulo and clear error`() = runTest {
        // Given
        val newTitulo = "Nuevo título de prueba"

        // When
        viewModel.onTituloChange(newTitulo)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(newTitulo, viewModel.crearTemaUiState.value.titulo)
        assertNull(viewModel.crearTemaUiState.value.error)
    }

    @Test
    fun `onContenidoChange should update contenido and clear error`() = runTest {
        // Given
        val newContenido = "Contenido de prueba para el tema"

        // When
        viewModel.onContenidoChange(newContenido)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(newContenido, viewModel.crearTemaUiState.value.contenido)
        assertNull(viewModel.crearTemaUiState.value.error)
    }

    @Test
    fun `validarYCrearTema should return false and set error when titulo is blank`() = runTest {
        // Given
        viewModel.onTituloChange("")
        viewModel.onContenidoChange("Contenido válido")
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        val result = viewModel.validarYCrearTema(authorId = 1L)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertFalse(result)
        assertEquals(
            "El título y el contenido no pueden estar vacíos",
            viewModel.crearTemaUiState.value.error
        )
        coVerify(exactly = 0) { repository.insertarTema(any()) }
    }

    @Test
    fun `validarYCrearTema should return false and set error when contenido is blank`() = runTest {
        // Given
        viewModel.onTituloChange("Título válido")
        viewModel.onContenidoChange("")
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        val result = viewModel.validarYCrearTema(authorId = 1L)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertFalse(result)
        assertEquals(
            "El título y el contenido no pueden estar vacíos",
            viewModel.crearTemaUiState.value.error
        )
        coVerify(exactly = 0) { repository.insertarTema(any()) }
    }

    @Test
    fun `validarYCrearTema should return true and call repository when both fields are valid`() =
        runTest {
            // Given
            val titulo = "Título válido"
            val contenido = "Contenido válido"
            val authorId = 1L
            viewModel.onTituloChange(titulo)
            viewModel.onContenidoChange(contenido)
            testDispatcher.scheduler.advanceUntilIdle()

            // When
            val result = viewModel.validarYCrearTema(authorId = authorId)
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            assertTrue(result)
            coVerify {
                repository.insertarTema(match {
                    it.titulo == titulo && it.contenido == contenido && it.authorId == authorId
                })
            }
        }

    @Test
    fun `validarYCrearTema should reset state after successful creation`() = runTest {
        // Given
        viewModel.onTituloChange("Título válido")
        viewModel.onContenidoChange("Contenido válido")
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.validarYCrearTema(authorId = 1L)
        testDispatcher.scheduler.advanceUntilIdle()

        // Entonces - El estado debe reiniciarse
        assertEquals("", viewModel.crearTemaUiState.value.titulo)
        assertEquals("", viewModel.crearTemaUiState.value.contenido)
        assertNull(viewModel.crearTemaUiState.value.error)
    }

    @Test
    fun `seleccionarTema should update temaSeleccionado state`() = runTest {
        // Given
        val temaId = 1L
        val tema = Tema(id = temaId, titulo = "Tema Test", contenido = "Contenido Test", authorId = 1L)
        every { repository.obtenerTemaPorId(temaId) } returns flowOf(tema)

        // When
        viewModel.seleccionarTema(temaId.toInt())
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(tema, viewModel.temaSeleccionado.value)
        verify { repository.obtenerTemaPorId(temaId) }
    }

    @Test
    fun `eliminarTema should call repository eliminarTema`() = runTest {
        // Given
        val tema = Tema(id = 1L, titulo = "Tema a eliminar", contenido = "Contenido", authorId = 1L)

        // When
        viewModel.eliminarTema(tema)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { repository.eliminarTema(tema) }
    }
}
