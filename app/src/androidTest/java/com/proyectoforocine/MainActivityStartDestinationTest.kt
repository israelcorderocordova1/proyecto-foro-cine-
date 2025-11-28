package com.proyectoforocine

import android.content.Intent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityStartDestinationTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun startsAtLogin_byDefault() {
        // Verificar que el destino inicial es login por defecto usando la actividad de la regla
        val destino = composeRule.activity.obtenerDestinoInicial()
        assertEquals("login", destino)
        composeRule.onNodeWithTag("navHostRoot").assertIsDisplayed()
    }

    @Test
    fun startsAtListaTemas_whenExtraSet() {
        val intent = Intent(composeRule.activity.applicationContext, MainActivity::class.java).apply {
            putExtra("startInList", true)
        }
        ActivityScenario.launch<MainActivity>(intent).use { scenario ->
            // Verificar el destino inicial usando el método público
            scenario.onActivity { activity ->
                val destino = activity.obtenerDestinoInicial()
                assertEquals("lista_temas", destino)
            }
        }
    }
}
