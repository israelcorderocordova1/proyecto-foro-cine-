package com.proyectoforocine

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityLifecycleTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun navHost_isVisible_onCreate() {
        composeRule.onNodeWithTag("navHostRoot").assertIsDisplayed()
    }

    @Test
    fun navHost_isVisible_afterRecreate() {
        composeRule.activityRule.scenario.recreate()
        composeRule.onNodeWithTag("navHostRoot").assertIsDisplayed()
    }

    @Test
    fun navHost_isVisible_afterLifecycleChanges() {
        val scenario = composeRule.activityRule.scenario
        scenario.moveToState(Lifecycle.State.CREATED)
        scenario.moveToState(Lifecycle.State.STARTED)
        scenario.moveToState(Lifecycle.State.RESUMED)
        composeRule.onNodeWithTag("navHostRoot").assertIsDisplayed()
    }
}
