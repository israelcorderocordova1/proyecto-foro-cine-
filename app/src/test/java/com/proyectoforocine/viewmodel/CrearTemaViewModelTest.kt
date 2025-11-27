package com.proyectoforocine.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.proyectoforocine.data.local.Tema
import com.proyectoforocine.data.local.TemaRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CrearTemaViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: TemaRepository
    private lateinit var viewModel: CrearTemaViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        viewModel = CrearTemaViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `insertarTema should create Tema with correct title and content`() = runTest {
        // Dado
        val titulo = "Mi título de prueba"
        val contenido = "Mi contenido de prueba"
        val temaSlot = slot<Tema>()
        
        // Cuando
        viewModel.insertarTema(titulo, contenido)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Entonces
        coVerify { repository.insertarTema(capture(temaSlot)) }
        assert(temaSlot.captured.titulo == titulo)
        assert(temaSlot.captured.contenido == contenido)
    }

    @Test
    fun `insertarTema should call repository insertarTema`() = runTest {
        // Dado
        val titulo = "Título"
        val contenido = "Contenido"
        
        // Cuando
        viewModel.insertarTema(titulo, contenido)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Entonces
        coVerify(exactly = 1) { repository.insertarTema(any()) }
    }

    @Test
    fun `insertarTema should handle empty strings`() = runTest {
        // Dado
        val titulo = ""
        val contenido = ""
        
        // Cuando
        viewModel.insertarTema(titulo, contenido)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Entonces
        coVerify { repository.insertarTema(match { 
            it.titulo.isEmpty() && it.contenido.isEmpty() 
        }) }
    }
}
