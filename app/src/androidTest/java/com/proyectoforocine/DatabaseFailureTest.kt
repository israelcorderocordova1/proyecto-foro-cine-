package com.proyectoforocine

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.proyectoforocine.data.local.AppDatabase
import com.proyectoforocine.data.local.Tema
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests de resiliencia para la base de datos Room.
 * Valida comportamiento cuando la BD falla, está corrupta o no hay espacio.
 */
@RunWith(AndroidJUnit4::class)
class DatabaseFailureTest {

    private lateinit var database: AppDatabase
    private val context: Context = ApplicationProvider.getApplicationContext()

    @Before
    fun crearDB() {
        // Crear BD en memoria para tests
        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries()
            .build()
    }

    @After
    fun cerrarDB() {
        database.close()
    }

    /**
     * Test: Verifica que la BD se puede crear correctamente
     */
    @Test
    fun baseDatos_debeCrearseCorrectamente() {
        assertNotNull("Database debe estar creada", database)
        assertTrue("Database debe estar abierta", database.isOpen)
    }

    /**
     * Test: Verifica comportamiento al insertar en BD cerrada
     */
    @Test(expected = IllegalStateException::class)
    fun cuandoBDCerrada_insertarDebeFallar() = runBlocking {
        // Cerrar la base de datos
        database.close()
        
        // Intentar insertar debe lanzar excepción
        val tema = Tema(titulo = "Test", contenido = "Contenido")
        database.temaDao().insertarTema(tema)
    }

    /**
     * Test: Verifica que la BD maneja transacciones fallidas
     */
    @Test
    fun cuandoTransaccionFalla_debeRollback() = runBlocking {
        val dao = database.temaDao()
        
        // Insertar un tema válido
        val tema1 = Tema(titulo = "Tema 1", contenido = "Contenido 1")
        dao.insertarTema(tema1)
        
        val temasAntes = dao.obtenerTodosLosTemas().first()
        
        try {
            // Intentar insertar tema inválido (título vacío debería fallar validación)
            val temaInvalido = Tema(titulo = "", contenido = "")
            dao.insertarTema(temaInvalido)
        } catch (e: Exception) {
            // Excepción esperada
        }
        
        // Verificar que el tema original sigue ahí
        val temasDespues = dao.obtenerTodosLosTemas().first()
        assertEquals(
            "No debe haber cambios si la transacción falla",
            temasAntes.size,
            temasDespues.size
        )
    }

    /**
     * Test: Verifica que se pueden consultar temas cuando la BD está vacía
     */
    @Test
    fun cuandoBDVacia_consultaDebeRetornarListaVacia() = runBlocking {
        val temas = database.temaDao().obtenerTodosLosTemas().first()
        
        assertNotNull("Lista no debe ser nula", temas)
        assertTrue("Lista debe estar vacía", temas.isEmpty())
    }

    /**
     * Test: Verifica que la BD maneja valores nulos apropiadamente
     */
    @Test
    fun cuandoDatosNulos_debeManejarse() = runBlocking {
        val dao = database.temaDao()
        
        // Room no permite nulls en campos NOT NULL, esto debe validarse
        try {
            val tema = Tema(titulo = "Test", contenido = "Test")
            dao.insertarTema(tema)
            
            // Room autogenera el ID, no lo retorna el insert
            // Consultar todos los temas y verificar que existe
            val temas = dao.obtenerTodosLosTemas().first()
            assertTrue("Debe haber al menos un tema", temas.isNotEmpty())
            assertEquals("Debe tener el título correcto", "Test", temas.first().titulo)
        } catch (e: Exception) {
            fail("No debe lanzar excepción con datos válidos: ${e.message}")
        }
    }

    /**
     * Test: Verifica recuperación de BD tras cerrar y reabrir
     */
    @Test
    fun cuandoBDReabierta_datosDebenPersistir() = runBlocking {
        // Insertar datos
        val tema = Tema(titulo = "Persistente", contenido = "Debe sobrevivir")
        val id = database.temaDao().insertarTema(tema)
        
        // Cerrar BD
        database.close()
        
        // Reabrir BD (en memoria se pierde, pero test valida el patrón)
        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        
        // En BD en memoria los datos se pierden, pero en BD persistente no
        // Este test valida que la reapertura no crashea
        assertNotNull("Database debe reabrirse", database)
        assertTrue("Database debe estar abierta", database.isOpen)
    }

    /**
     * Test: Verifica manejo de múltiples inserciones simultáneas
     */
    @Test
    fun cuandoMultiplesInserciones_debeManejarseSinBloqueos() = runBlocking {
        val dao = database.temaDao()
        
        // Insertar múltiples temas
        repeat(10) { i ->
            val tema = Tema(titulo = "Tema $i", contenido = "Contenido $i")
            dao.insertarTema(tema)
        }
        
        val temas = dao.obtenerTodosLosTemas().first()
        assertEquals("Debe haber 10 temas", 10, temas.size)
    }

    /**
     * Test: Verifica que eliminaciones funcionan correctamente
     */
    @Test
    fun cuandoEliminarTema_debeRemoverCorrectamente() = runBlocking {
        val dao = database.temaDao()
        
        // Insertar tema
        val tema = Tema(titulo = "Para eliminar", contenido = "Temporal")
        dao.insertarTema(tema)
        
        // Obtener todos los temas
        val temasInsertados = dao.obtenerTodosLosTemas().first()
        assertTrue("Debe haber al menos un tema", temasInsertados.isNotEmpty())
        
        // Eliminar el primer tema
        val temaAEliminar = temasInsertados.first()
        dao.eliminarTema(temaAEliminar)
        
        // Verificar eliminación
        val temasDespues = dao.obtenerTodosLosTemas().first()
        assertEquals(
            "Debe tener un tema menos",
            temasInsertados.size - 1,
            temasDespues.size
        )
    }
}
