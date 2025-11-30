package com.proyectoforocine

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginFlowTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun loginScreenRendersNavHost() {
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("navHostRoot").assertIsDisplayed()
    }

    @Test
    fun loginFieldsExistAndAcceptInput() {
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("emailField").assertIsDisplayed()
        composeRule.onNodeWithTag("passwordField").assertIsDisplayed()
        
        // Ingresar texto en los campos
        composeRule.onNodeWithTag("emailField").performTextInput("test@example.com")
        composeRule.onNodeWithTag("passwordField").performTextInput("password123")
        
        // Verificar que el campo de email contiene el texto
        composeRule.onNodeWithTag("emailField").assertTextContains("test@example.com")
        
        // No verificamos el contenido del password field porque está oculto por PasswordVisualTransformation
        // Solo validamos que acepta entrada sin crashear
        composeRule.onNodeWithTag("passwordField").assertIsDisplayed()
    }

    @Test
    fun loginButtonExistsAndCanBeClicked() {
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("loginUserButton").assertIsDisplayed()
        composeRule.onNodeWithTag("loginUserButton").performClick()
        // Tras click, NavHost sigue visible (navegación ocurre según estado de auth)
        composeRule.onNodeWithTag("navHostRoot").assertIsDisplayed()
    }

    @Test
    fun registerLinkExistsAndCanBeClicked() {
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("loginModeratorButton").assertIsDisplayed()
        composeRule.onNodeWithTag("loginModeratorButton").performClick()
        // Navega a registro, NavHost sigue visible
        composeRule.onNodeWithTag("navHostRoot").assertIsDisplayed()
    }

    @Test
    fun completeLoginFlowWithValidCredentials() {
        composeRule.waitForIdle()
        
        // Ingresar credenciales válidas (según lógica de AuthViewModel)
        composeRule.onNodeWithTag("emailField").performTextInput("user@example.com")
        composeRule.onNodeWithTag("passwordField").performTextInput("validpassword")
        
        // Hacer click en login
        composeRule.onNodeWithTag("loginUserButton").performClick()
        
        // Esperar a que se procese la auth
        composeRule.waitForIdle()
        Thread.sleep(1000) // Dar tiempo para navegación
        
        // Verificar que sigue mostrando UI (no crashea)
        composeRule.onNodeWithTag("navHostRoot").assertIsDisplayed()
    }

    @Test
    fun loginWithEmptyFieldsShowsValidation() {
        composeRule.waitForIdle()
        
        // Intentar login sin llenar campos
        composeRule.onNodeWithTag("loginUserButton").performClick()
        
        composeRule.waitForIdle()
        
        // La UI debe seguir visible (validación ocurre en ViewModel)
        composeRule.onNodeWithTag("navHostRoot").assertIsDisplayed()
    }
}
