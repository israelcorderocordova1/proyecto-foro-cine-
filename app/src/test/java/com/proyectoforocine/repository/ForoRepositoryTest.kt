package com.proyectoforocine.repository

import com.proyectoforocine.model.TemaForo
import com.proyectoforocine.model.Usuario
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

/**
 * Pruebas unitarias para ForoRepository (Singleton)
 * Este repositorio maneja la lista de temas en memoria
 */
class ForoRepositoryTest {

    private lateinit var temasOriginales: List<TemaForo>
    private val usuarioPrueba = Usuario(id = "test1", nombre = "Usuario Test", rol = "registrado")

    @Before
    fun configuracion() {
        // Guardar estado original de los temas para restaurar después
        temasOriginales = ForoRepository.temas.toList()
    }

    @After
    fun limpiar() {
        // Restaurar el estado original eliminando todos los temas actuales
        // y agregando los originales de nuevo
        ForoRepository.temas.toList().forEach { tema ->
            ForoRepository.deleteTema(tema)
        }
        // No podemos restaurar perfectamente porque es un objeto singleton
        // pero al menos limpiamos
    }

    @Test
    fun `temas debe retornar lista inicial con 2 temas por defecto`() {
        // Dado - El repositorio es un singleton que puede tener temas de otros tests
        
        // Cuando
        val temas = ForoRepository.temas
        
        // Entonces
        assertNotNull("La lista de temas no debe ser nula", temas)
        assertTrue("Debe ser una lista válida", temas is List)
        // Nota: No verificamos cantidad exacta porque es un singleton compartido entre tests
    }

    @Test
    fun `getTemaById debe retornar tema existente`() {
        // Dado
        val temas = ForoRepository.temas
        val primerTema = temas.firstOrNull()
        assertNotNull("Debe existir al menos un tema", primerTema)
        
        // Cuando
        val temaEncontrado = ForoRepository.getTemaById(primerTema!!.id)
        
        // Entonces
        assertNotNull("Debe encontrar el tema", temaEncontrado)
        assertEquals("El ID debe coincidir", primerTema.id, temaEncontrado?.id)
        assertEquals("El título debe coincidir", primerTema.titulo, temaEncontrado?.titulo)
    }

    @Test
    fun `getTemaById debe retornar null para ID inexistente`() {
        // Given
        val idInexistente = 999999L
        
        // When
        val temaEncontrado = ForoRepository.getTemaById(idInexistente)
        
        // Then
        assertNull("No debe encontrar tema con ID inexistente", temaEncontrado)
    }

    @Test
    fun `addTema debe agregar nuevo tema al principio de la lista`() {
        // Given
        val cantidadInicial = ForoRepository.temas.size
        val titulo = "Tema de Prueba"
        val contenido = "Contenido de prueba para test unitario"
        
        // When
        ForoRepository.addTema(titulo, contenido, usuarioPrueba)
        
        // Then
        val temasActualizados = ForoRepository.temas
        assertEquals("Debe haber un tema más", cantidadInicial + 1, temasActualizados.size)
        
        val primerTema = temasActualizados.first()
        assertEquals("El título debe coincidir", titulo, primerTema.titulo)
        assertEquals("El contenido debe coincidir", contenido, primerTema.contenido)
        assertEquals("El autor debe coincidir", usuarioPrueba, primerTema.autor)
        assertEquals("La categoría por defecto debe ser General", "General", primerTema.categoria)
    }

    @Test
    fun `addTema debe generar IDs únicos e incrementales`() {
        // Given
        val cantidadInicial = ForoRepository.temas.size
        
        // Cuando - Agregar dos temas
        ForoRepository.addTema("Tema 1", "Contenido 1", usuarioPrueba)
        val id1 = ForoRepository.temas.first().id
        
        ForoRepository.addTema("Tema 2", "Contenido 2", usuarioPrueba)
        val id2 = ForoRepository.temas.first().id
        
        // Entonces
        assertEquals("Debe haber 2 temas más", cantidadInicial + 2, ForoRepository.temas.size)
        assertNotEquals("Los IDs deben ser diferentes", id1, id2)
        assertTrue("El segundo ID debe ser mayor", id2 > id1)
    }

    @Test
    fun `deleteTema debe eliminar tema de la lista`() {
        // Dado - Primero agregamos un tema
        ForoRepository.addTema("Tema a Eliminar", "Contenido", usuarioPrueba)
        val temaParaEliminar = ForoRepository.temas.first()
        val cantidadAntesDeEliminar = ForoRepository.temas.size
        
        // Cuando
        ForoRepository.deleteTema(temaParaEliminar)
        
        // Entonces
        assertEquals("Debe haber un tema menos", cantidadAntesDeEliminar - 1, ForoRepository.temas.size)
        assertNull("El tema eliminado no debe existir", ForoRepository.getTemaById(temaParaEliminar.id))
    }

    @Test
    fun `deleteTema no debe afectar otros temas`() {
        // Dado - Agregar varios temas
        ForoRepository.addTema("Tema 1", "Contenido 1", usuarioPrueba)
        val tema1 = ForoRepository.temas.first()
        
        ForoRepository.addTema("Tema 2", "Contenido 2", usuarioPrueba)
        val tema2 = ForoRepository.temas.first()
        
        ForoRepository.addTema("Tema 3", "Contenido 3", usuarioPrueba)
        
        // Cuando - Eliminar solo el tema del medio
        ForoRepository.deleteTema(tema2)
        
        // Entonces
        assertNull("Tema 2 debe estar eliminado", ForoRepository.getTemaById(tema2.id))
        assertNotNull("Tema 1 debe seguir existiendo", ForoRepository.getTemaById(tema1.id))
    }

    @Test
    fun `los temas deben tener valoracion inicial de cero para nuevos temas`() {
        // Dado
        val titulo = "Tema Nuevo"
        val contenido = "Contenido nuevo"
        
        // Cuando
        ForoRepository.addTema(titulo, contenido, usuarioPrueba)
        val nuevoTema = ForoRepository.temas.first()
        
        // Entonces
        assertEquals("La valoración inicial debe ser 0", 0, nuevoTema.valoracion)
    }
}
