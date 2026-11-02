package com.skilltest.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.skilltest.design.theme.HiTunesTheme
import com.skilltest.ui.model.UiTrack
import com.skilltest.ui.viewmodel.SongsListIntent
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
    fun searchFieldReceivesFocusAfterOpeningAnimation() {
        var isSearching by mutableStateOf(false)
        composeRule.setContent {
            HiTunesTheme {
                Column {
                    SongsListHeader(
                        state = SongsListState(isSearching = isSearching),
                        onIntent = { if (it == SongsListIntent.SearchClick) isSearching = true },
                    )
                }
            }
        }

        composeRule.onNodeWithContentDescription("Search a song").performClick()
        composeRule.onNode(hasSetTextAction()).assertIsFocused()
    }

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
