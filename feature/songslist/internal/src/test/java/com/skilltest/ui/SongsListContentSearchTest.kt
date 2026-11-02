package com.skilltest.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.skilltest.design.theme.HiTunesTheme
import com.skilltest.ui.model.UiTrack
import com.skilltest.ui.viewmodel.SongsListState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35])
class SongsListContentSearchTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shortQueryHidesRecentlyPlayedAndStaleResults() {
        composeRule.setContent {
            HiTunesTheme {
                SongsListContent(
                    state = SongsListState(
                        isSearching = true,
                        searchTerm = "ab",
                        recentlyPlayed = listOf(recentTrack),
                        searchResult = listOf(searchTrack),
                    ),
                    onIntent = {},
                )
            }
        }

        composeRule.onNodeWithText("Recently played").assertDoesNotExist()
        composeRule.onNodeWithText("Recent song").assertDoesNotExist()
        composeRule.onNodeWithText("Search song").assertDoesNotExist()
    }

    @Test
    fun searchResultsShowWithoutRecentlyPlayedHeading() {
        composeRule.setContent {
            HiTunesTheme {
                SongsListContent(
                    state = SongsListState(
                        isSearching = true,
                        searchTerm = "jazz",
                        recentlyPlayed = listOf(recentTrack),
                        searchResult = listOf(searchTrack),
                    ),
                    onIntent = {},
                )
            }
        }

        composeRule.onNodeWithText("Search song").assertIsDisplayed()
        composeRule.onNodeWithText("Recently played").assertDoesNotExist()
        composeRule.onNodeWithText("Recent song").assertDoesNotExist()
    }

    private companion object {
        val recentTrack = UiTrack("recent", "Recent song", "Artist", "", false)
        val searchTrack = UiTrack("search", "Search song", "Artist", "", false)
    }
}
