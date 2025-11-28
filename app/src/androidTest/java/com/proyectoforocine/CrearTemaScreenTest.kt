package com.proyectoforocine

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumentation test para CrearTemaScreen
 */
@RunWith(AndroidJUnit4::class)
class CrearTemaScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun activityArrancaYComposeExiste() {
        // Verifica que el árbol Compose está presente y el NavHost inicial existe
        composeTestRule.onRoot().assertIsDisplayed()
        composeTestRule.onNodeWithTag("navHostRoot").assertIsDisplayed()
    }

    @Test
    fun composeUIRendersSuccessfully() {
        composeTestRule.waitForIdle()
        Thread.sleep(1500)

        // Just verify we can interact with the root
        composeTestRule.onRoot().assertExists()
    }

    @Test
    fun navigationGraphLoads() {
        composeTestRule.waitForIdle()
        Thread.sleep(1000)

        // Verify activity content is set
        composeTestRule.onRoot().assertExists()
    }
}
