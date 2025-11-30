package com.proyectoforocine

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Prueba de UI básica para verificar que el NavHost se renderiza
 * al iniciar la aplicación.
 */
@RunWith(AndroidJUnit4::class)
class MainActivityUiTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun navHostRootSeRenderizaAlArrancar() {
        composeTestRule.onNodeWithTag("navHostRoot").assertIsDisplayed()
    }
}
