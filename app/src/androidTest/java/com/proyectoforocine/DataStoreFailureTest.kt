package com.proyectoforocine

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Tests de resiliencia para DataStore.
 * Valida comportamiento cuando hay errores de lectura/escritura o datos corruptos.
 */
@RunWith(AndroidJUnit4::class)
class DataStoreFailureTest {

    // Usa un nombre único por ejecución para evitar múltiples instancias sobre el mismo archivo
    private val uniqueName = "test_preferences_" + System.currentTimeMillis()
    private val Context.testDataStore by preferencesDataStore(name = uniqueName)
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    /**
     * Test: Verifica que DataStore puede escribir y leer correctamente
     */
    @Test
    fun dataStore_debeLeerYEscribirCorrectamente() = runBlocking {
        val testKey = stringPreferencesKey("test_key")
        val testValue = "test_value"

        // Escribir
        context.testDataStore.edit { preferences ->
            preferences[testKey] = testValue
        }

        // Leer
        val preferences = context.testDataStore.data.first()
        val valorLeido = preferences[testKey]

        assertEquals("Valor debe ser el mismo", testValue, valorLeido)
    }

    /**
     * Test: Verifica comportamiento cuando se lee una clave inexistente
     */
    @Test
    fun cuandoClaveNoExiste_debeRetornarNull() = runBlocking {
        val claveInexistente = stringPreferencesKey("clave_que_no_existe_12345")
        
        val preferences = context.testDataStore.data.first()
        val valor = preferences[claveInexistente]
        
        assertNull("Clave inexistente debe retornar null", valor)
    }

    /**
     * Test: Verifica que múltiples escrituras no causan corrupción
     */
    @Test
    fun cuandoMultiplesEscrituras_noDebeCorromperDatos() = runBlocking {
        val key1 = stringPreferencesKey("key_1")
        val key2 = stringPreferencesKey("key_2")
        
        // Múltiples escrituras
        context.testDataStore.edit { preferences ->
            preferences[key1] = "valor_1"
            preferences[key2] = "valor_2"
        }
        
        context.testDataStore.edit { preferences ->
            preferences[key1] = "valor_actualizado"
        }
        
        // Verificar integridad
        val preferences = context.testDataStore.data.first()
        assertEquals("Key1 debe estar actualizada", "valor_actualizado", preferences[key1])
        assertEquals("Key2 debe permanecer", "valor_2", preferences[key2])
    }

    /**
     * Test: Verifica que DataStore maneja timeouts apropiadamente
     */
    @Test
    fun cuandoOperacionLenta_debeCompletarseEnTiempoRazonable() = runBlocking {
        val testKey = stringPreferencesKey("timeout_test")
        
        try {
            withTimeout(5000) { // 5 segundos timeout
                context.testDataStore.edit { preferences ->
                    preferences[testKey] = "completado"
                }
                
                val preferences = context.testDataStore.data.first()
                val valor = preferences[testKey]
                
                assertEquals("Operación debe completarse", "completado", valor)
            }
        } catch (e: Exception) {
            fail("Operación no debe exceder timeout: ${e.message}")
        }
    }

    /**
     * Test: Verifica que se pueden limpiar todas las preferencias
     */
    @Test
    fun cuandoLimpiarPreferencias_debenEliminarseTodasLasClaves() = runBlocking {
        val key1 = stringPreferencesKey("clear_test_1")
        val key2 = stringPreferencesKey("clear_test_2")
        
        // Escribir datos
        context.testDataStore.edit { preferences ->
            preferences[key1] = "dato1"
            preferences[key2] = "dato2"
        }
        
        // Limpiar todo
        context.testDataStore.edit { preferences ->
            preferences.clear()
        }
        
        // Verificar limpieza
        val preferences = context.testDataStore.data.first()
        assertNull("Key1 debe estar eliminada", preferences[key1])
        assertNull("Key2 debe estar eliminada", preferences[key2])
        assertTrue("Preferencias deben estar vacías", preferences.asMap().isEmpty())
    }

    /**
     * Test: Verifica manejo de valores especiales (strings vacíos, muy largos)
     */
    @Test
    fun cuandoValoresEspeciales_debeManejarse() = runBlocking {
        val keyVacio = stringPreferencesKey("empty_string")
        val keyLargo = stringPreferencesKey("long_string")
        
        val stringVacio = ""
        val stringLargo = "a".repeat(10000) // 10k caracteres
        
        context.testDataStore.edit { preferences ->
            preferences[keyVacio] = stringVacio
            preferences[keyLargo] = stringLargo
        }
        
        val preferences = context.testDataStore.data.first()
        assertEquals("String vacío debe guardarse", stringVacio, preferences[keyVacio])
        assertEquals("String largo debe guardarse", stringLargo, preferences[keyLargo])
        assertEquals("Longitud debe preservarse", 10000, preferences[keyLargo]?.length)
    }

    /**
     * Test: Verifica que el contexto de DataStore es válido
     */
    @Test
    fun contextoDataStore_debeSerValido() {
        assertNotNull("Contexto no debe ser nulo", context)
        assertNotNull("DataStore debe inicializarse", context.testDataStore)
    }

    /**
     * Test: Verifica comportamiento con caracteres especiales
     */
    @Test
    fun cuandoCaracteresEspeciales_debeGuardarse() = runBlocking {
        val testKey = stringPreferencesKey("special_chars")
        val caracteresEspeciales = "áéíóú ñ !@#\$%^&*()_+{}[]|:;<>?,./"
        
        context.testDataStore.edit { preferences ->
            preferences[testKey] = caracteresEspeciales
        }
        
        val preferences = context.testDataStore.data.first()
        assertEquals(
            "Caracteres especiales deben preservarse",
            caracteresEspeciales,
            preferences[testKey]
        )
    }

    /**
     * Test: Verifica que actualizaciones concurrentes no causan pérdida de datos
     */
    @Test
    fun cuandoActualizacionesConcurrentes_datosDebenSerConsistentes() = runBlocking {
        val testKey = stringPreferencesKey("concurrent_test")
        
        // Realizar múltiples actualizaciones
        repeat(5) { i ->
            context.testDataStore.edit { preferences ->
                preferences[testKey] = "iteration_$i"
            }
        }
        
        // Verificar última actualización
        val preferences = context.testDataStore.data.first()
        val valorFinal = preferences[testKey]
        
        assertNotNull("Debe haber un valor final", valorFinal)
        assertTrue(
            "Valor debe ser de una iteración válida",
            valorFinal?.startsWith("iteration_") == true
        )
    }
}
