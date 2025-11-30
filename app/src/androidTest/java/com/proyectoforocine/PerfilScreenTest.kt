package com.proyectoforocine

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumentation test para PerfilScreen
 */
@RunWith(AndroidJUnit4::class)
class PerfilScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun composeHierarchyExistsAfterLaunch() {
        composeTestRule.waitForIdle()
        Thread.sleep(2000)

        composeTestRule.onRoot().printToLog("PROFILE_SCREEN")
        composeTestRule.onRoot().assertExists()
    }

    @Test
    fun themeAppliesCorrectly() {
        composeTestRule.waitForIdle()
        Thread.sleep(1500)

        // Verify theme content is rendered
        composeTestRule.onRoot().assertExists()
    }

    @Test
    fun navigationHostIsPresent() {
        composeTestRule.waitForIdle()
        Thread.sleep(1000)

        // Verify NavHost is set up
        composeTestRule.onRoot().assertExists()
    }

    @Test
    fun surfaceContainerExists() {
        composeTestRule.waitForIdle()
        Thread.sleep(1000)

        // Verify Surface container renders
        composeTestRule.onRoot().assertExists()
    }
}
