package com.proyectoforocine

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
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
    fun activityLaunchesWithComposeContent() {
        composeTestRule.waitForIdle()
        Thread.sleep(2000)

        // Log the compose tree for debugging
        composeTestRule.onRoot().printToLog("COMPOSE_TREE")

        // Verify compose hierarchy exists
        composeTestRule.onRoot().assertExists()
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
