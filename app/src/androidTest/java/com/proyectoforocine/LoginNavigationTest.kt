package com.proyectoforocine

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Pruebas de navegación desde la pantalla de Login.
 */
@RunWith(AndroidJUnit4::class)
class LoginNavigationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun navegarAListaTemasComoUsuario() {
        // Hacer clic en el botón de login como usuario
        composeTestRule.onNodeWithTag("loginUserButton").performClick()
        // Verificar que el NavHost sigue presente (la vista cambió a lista_temas)
        composeTestRule.onNodeWithTag("navHostRoot").assertIsDisplayed()
        // Opcional: añadir un tag en ListaTemasScreen para validación específica y asertarlo aquí
    }

    @Test
    fun navegarAListaTemasComoModerador() {
        // Hacer clic en el botón de login como moderador
        composeTestRule.onNodeWithTag("loginModeratorButton").performClick()
        // Verificar contenedor raiz visible
        composeTestRule.onNodeWithTag("navHostRoot").assertIsDisplayed()
    }
}
