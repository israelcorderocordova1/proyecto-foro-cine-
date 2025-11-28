package com.proyectoforocine

import com.proyectoforocine.utils.resolverDestinoInicial
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Test JUnit puro que cubre la función utilitaria de navegación usada por MainActivity.
 * Esto permite atribuir cobertura al archivo NavigationUtils.kt con tests unitarios.
 */
class MainActivityStartDestinationUnitTest {

    @Test
    fun resolverDestinoInicial_retornaLogin_cuandoFlagFalso() {
        val resultado = resolverDestinoInicial(false)
        assertEquals("login", resultado)
    }

    @Test
    fun resolverDestinoInicial_retornaLista_cuandoFlagVerdadero() {
        val resultado = resolverDestinoInicial(true)
        assertEquals("lista_temas", resultado)
    }
}
