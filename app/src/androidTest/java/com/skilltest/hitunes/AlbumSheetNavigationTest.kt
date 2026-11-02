package com.skilltest.hitunes

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso
import com.skilltest.hitunes.ui.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlbumSheetNavigationTest {
    @get:Rule val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun moreActionsOpensSheetAndViewAlbumOpensDetails() {
        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodesWithContentDescription("More actions")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onAllNodesWithContentDescription("More actions").onFirst().performClick()
        composeRule.onNodeWithText("View album").assertIsDisplayed()
        composeRule.onNodeWithText("View album").performClick()
        composeRule.onNodeWithText("album-1").assertIsDisplayed()
    }

    @Test
    fun unfinishedSearchQuerySurvivesActivityRecreation() {
        composeRule.onAllNodesWithContentDescription("Search a song").onFirst().performClick()
        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodes(hasSetTextAction()).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNode(hasSetTextAction()).performTextInput("ab")
        composeRule.onNode(hasSetTextAction()).assertTextContains("ab")

        composeRule.activityRule.scenario.recreate()

        composeRule.onNode(hasSetTextAction()).assertTextContains("ab")
    }

    @Test
    fun returningFromAlbumKeepsRecentlyPlayedRows() {
        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodesWithContentDescription("More actions")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onAllNodesWithContentDescription("More actions").onFirst().performClick()
        composeRule.onNodeWithText("View album").performClick()
        composeRule.onNodeWithText("album-1").assertIsDisplayed()

        Espresso.pressBack()

        composeRule.onNodeWithText("Song 1").assertIsDisplayed()
    }
}
