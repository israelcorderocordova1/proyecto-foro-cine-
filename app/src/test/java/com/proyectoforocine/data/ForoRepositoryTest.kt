package com.proyectoforocine.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.proyectoforocine.data.local.Tema
import com.proyectoforocine.data.local.TemaDao
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class ForoRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var temaDao: TemaDao
    private lateinit var repository: ForoRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        temaDao = mockk(relaxed = true)
        repository = ForoRepository(temaDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `todosLosTemas flow should emit data from DAO`() = runTest {
        // Dado
        val temasList = listOf(
            Tema(id = 1L, titulo = "Tema 1", contenido = "Contenido 1"),
            Tema(id = 2L, titulo = "Tema 2", contenido = "Contenido 2"),
            Tema(id = 3L, titulo = "Tema 3", contenido = "Contenido 3")
        )
        every { temaDao.obtenerTodosLosTemas() } returns flowOf(temasList)

        // Cuando
        val newRepository = ForoRepository(temaDao)
        val result = newRepository.todosLosTemas.first()

        // Entonces
        assertEquals(temasList, result)
        assertEquals(3, result.size)
        verify { temaDao.obtenerTodosLosTemas() }
    }

    @Test
    fun `todosLosTemas should return empty list when no temas exist`() = runTest {
        // Dado
        every { temaDao.obtenerTodosLosTemas() } returns flowOf(emptyList())

        // Cuando
        val newRepository = ForoRepository(temaDao)
        val result = newRepository.todosLosTemas.first()

        // Entonces
        assertTrue(result.isEmpty())
    }

    @Test
    fun `obtenerTemaPorId should return correct Flow from DAO`() = runTest {
        // Dado
        val temaId = 5L
        val tema = Tema(id = temaId, titulo = "Tema Específico", contenido = "Contenido Específico")
        every { temaDao.obtenerTemaPorId(temaId) } returns flowOf(tema)

        // Cuando
        val result = repository.obtenerTemaPorId(temaId).first()

        // Entonces
        assertEquals(tema, result)
        assertEquals(temaId, result?.id)
        verify { temaDao.obtenerTemaPorId(temaId) }
    }

    @Test
    fun `obtenerTemaPorId should return null when tema does not exist`() = runTest {
        // Dado
        val temaId = 999L
        every { temaDao.obtenerTemaPorId(temaId) } returns flowOf(null)

        // Cuando
        val result = repository.obtenerTemaPorId(temaId).first()

        // Entonces
        assertNull(result)
    }

    @Test
    fun `insertarTema should call DAO insertarTema`() = runTest {
        // Given
        val tema = Tema(titulo = "Nuevo Tema", contenido = "Nuevo Contenido")
        coEvery { temaDao.insertarTema(tema) } just Runs

        // When
        repository.insertarTema(tema)

        // Then
        coVerify(exactly = 1) { temaDao.insertarTema(tema) }
    }

    @Test
    fun `insertarTema should pass correct tema object to DAO`() = runTest {
        // Given
        val tema = Tema(id = 10L, titulo = "Tema Test", contenido = "Contenido Test")
        val temaSlot = slot<Tema>()
        coEvery { temaDao.insertarTema(capture(temaSlot)) } just Runs

        // When
        repository.insertarTema(tema)

        // Then
        assertEquals(tema.titulo, temaSlot.captured.titulo)
        assertEquals(tema.contenido, temaSlot.captured.contenido)
    }

    @Test
    fun `eliminarTema should call DAO eliminarTema`() = runTest {
        // Given
        val tema = Tema(id = 7L, titulo = "Tema a Eliminar", contenido = "Contenido")
        coEvery { temaDao.eliminarTema(tema) } just Runs

        // When
        repository.eliminarTema(tema)

        // Then
        coVerify(exactly = 1) { temaDao.eliminarTema(tema) }
    }

    @Test
    fun `eliminarTema should pass correct tema object to DAO`() = runTest {
        // Given
        val tema = Tema(id = 15L, titulo = "Tema Delete", contenido = "Delete Content")
        val temaSlot = slot<Tema>()
        coEvery { temaDao.eliminarTema(capture(temaSlot)) } just Runs

        // When
        repository.eliminarTema(tema)

        // Then
        assertEquals(tema.id, temaSlot.captured.id)
        assertEquals(tema.titulo, temaSlot.captured.titulo)
    }
}
