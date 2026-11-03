package com.skilltest.hitunes.wear

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.wear.compose.material3.MaterialTheme
import com.github.takahirom.roborazzi.captureRoboImage
import com.skilltest.core.wearsync.NowPlaying
import com.skilltest.core.wearsync.WearCommand
import com.skilltest.hitunes.wear.ui.ARTWORK_PLACEHOLDER_TAG
import com.skilltest.hitunes.wear.ui.WearPlayerContent
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35], qualifiers = "w227dp-h227dp-round-xhdpi")
class WearPlayerContentTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun nothingPlayingShowsPlaceholderAndDisablesEveryControl() {
        composeRule.setContent {
            MaterialTheme { WearPlayerContent(nowPlaying = null, onCommand = {}) }
        }

        composeRule.onNodeWithTag(ARTWORK_PLACEHOLDER_TAG).assertExists()
        composeRule.onNodeWithText("Nothing playing").assertExists()
        composeRule.onNodeWithContentDescription("previous song").assertIsNotEnabled()
        composeRule.onNodeWithContentDescription("play").assertIsNotEnabled()
        composeRule.onNodeWithContentDescription("next song").assertIsNotEnabled()
        composeRule.onRoot().captureRoboImage()
    }

    @Test
    fun playingSongShowsDetailsAndSendsCommands() {
        val sent = mutableListOf<WearCommand>()
        composeRule.setContent {
            MaterialTheme { WearPlayerContent(nowPlaying = playing, onCommand = { sent += it }) }
        }

        composeRule.onNodeWithTag(ARTWORK_PLACEHOLDER_TAG).assertDoesNotExist()
        composeRule.onNodeWithText("Thriller").assertExists()
        composeRule.onNodeWithContentDescription("previous song").assertIsNotEnabled()
        composeRule.onNodeWithContentDescription("next song").assertIsEnabled().performClick()
        composeRule.onNodeWithContentDescription("pause").assertIsEnabled().performClick()
        composeRule.onRoot().captureRoboImage()

        assertEquals(listOf(WearCommand.Next, WearCommand.Pause), sent)
    }

    private val playing = NowPlaying(
        title = "Thriller",
        artist = "Michael Jackson",
        artworkUrl = "",
        isPlaying = true,
        hasNext = true,
        hasPrevious = false,
    )
}
