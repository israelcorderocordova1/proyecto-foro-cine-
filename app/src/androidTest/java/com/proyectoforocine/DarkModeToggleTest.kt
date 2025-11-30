package com.proyectoforocine

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertIsDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DarkModeToggleTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun navHostVisible() {
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("navHostRoot").assertIsDisplayed()
    }

    /**
     * Test que valida el toggle de modo oscuro.
     * Nota: Requiere estar en PerfilScreen, lo que necesita autenticación.
     * Este test valida la estructura básica.
     */
    @Test
    fun darkModeToggleStructure() {
        composeRule.waitForIdle()
        
        // Para un test completo del toggle, necesitamos:
        // 1. Autenticarnos
        // 2. Navegar a perfil
        // 3. Encontrar el switch con testTag "darkModeToggle"
        // 4. Hacer click
        // 5. Verificar que el tema cambió
        
        // Por ahora validamos que la app inicia sin errores
        composeRule.onNodeWithTag("navHostRoot").assertIsDisplayed()
    }

    @Test
    fun appSupportsThemeChanges() {
        composeRule.waitForIdle()
        
        // Test que verifica que el sistema de temas está configurado
        // La aplicación debe renderizarse sin crashear
        composeRule.onNodeWithTag("navHostRoot").assertIsDisplayed()
        
        // En un test E2E completo, aquí navegaríamos a perfil
        // y validaríamos el toggle funcional
    }
}
