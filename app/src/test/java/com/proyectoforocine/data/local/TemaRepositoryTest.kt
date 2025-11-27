package com.proyectoforocine.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
class TemaRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var temaDao: TemaDao
    private lateinit var repository: TemaRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        temaDao = mockk(relaxed = true)
        repository = TemaRepository(temaDao)
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
            Tema(id = 1L, titulo = "Tema A", contenido = "Contenido A"),
            Tema(id = 2L, titulo = "Tema B", contenido = "Contenido B")
        )
        every { temaDao.obtenerTodosLosTemas() } returns flowOf(temasList)
        
        // Cuando
        val newRepository = TemaRepository(temaDao)
        val result = newRepository.todosLosTemas.first()
        
        // Entonces
        assertEquals(temasList, result)
        assertEquals(2, result.size)
    }

    @Test
    fun `todosLosTemas should return empty list when no data exists`() = runTest {
        // Dado
        every { temaDao.obtenerTodosLosTemas() } returns flowOf(emptyList())
        
        // Cuando
        val newRepository = TemaRepository(temaDao)
        val result = newRepository.todosLosTemas.first()
        
        // Entonces
        assertTrue(result.isEmpty())
        assertEquals(0, result.size)
    }

    @Test
    fun `insertarTema should call DAO insertarTema`() = runTest {
        // Dado
        val tema = Tema(titulo = "Tema Nuevo", contenido = "Contenido Nuevo")
        coEvery { temaDao.insertarTema(tema) } just Runs
        
        // Cuando
        repository.insertarTema(tema)
        
        // Entonces
        coVerify(exactly = 1) { temaDao.insertarTema(tema) }
    }

    @Test
    fun `insertarTema should pass correct tema to DAO`() = runTest {
        // Dado
        val tema = Tema(id = 99L, titulo = "Test Tema", contenido = "Test Contenido")
        val temaSlot = slot<Tema>()
        coEvery { temaDao.insertarTema(capture(temaSlot)) } just Runs
        
        // Cuando
        repository.insertarTema(tema)
        
        // Entonces
        assertEquals(tema.id, temaSlot.captured.id)
        assertEquals(tema.titulo, temaSlot.captured.titulo)
        assertEquals(tema.contenido, temaSlot.captured.contenido)
    }

    @Test
    fun `eliminarTema should call DAO eliminarTema`() = runTest {
        // Given
        val tema = Tema(id = 50L, titulo = "Tema Delete", contenido = "Delete")
        coEvery { temaDao.eliminarTema(tema) } just Runs
        
        // When
        repository.eliminarTema(tema)
        
        // Then
        coVerify(exactly = 1) { temaDao.eliminarTema(tema) }
    }

    @Test
    fun `eliminarTema should pass correct tema to DAO`() = runTest {
        // Given
        val tema = Tema(id = 77L, titulo = "Remove This", contenido = "Content")
        val temaSlot = slot<Tema>()
        coEvery { temaDao.eliminarTema(capture(temaSlot)) } just Runs
        
        // When
        repository.eliminarTema(tema)
        
        // Then
        assertEquals(tema.id, temaSlot.captured.id)
    }

    @Test
    fun `multiple insertarTema calls should be executed sequentially`() = runTest {
        // Given
        val tema1 = Tema(titulo = "Tema 1", contenido = "Contenido 1")
        val tema2 = Tema(titulo = "Tema 2", contenido = "Contenido 2")
        val tema3 = Tema(titulo = "Tema 3", contenido = "Contenido 3")
        coEvery { temaDao.insertarTema(any()) } just Runs
        
        // When
        repository.insertarTema(tema1)
        repository.insertarTema(tema2)
        repository.insertarTema(tema3)
        
        // Then
        coVerify(exactly = 3) { temaDao.insertarTema(any()) }
    }
}
