package com.skilltest.hitunes

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.test.ext.junit.runners.AndroidJUnit4
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
}
