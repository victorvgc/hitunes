package com.skilltest.hitunes

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skilltest.data.di.repositoryModule
import com.skilltest.domain.model.Album
import com.skilltest.domain.model.ResultWrapper
import com.skilltest.domain.model.Track
import com.skilltest.domain.repository.AlbumRepository
import com.skilltest.domain.repository.SongRepository
import com.skilltest.hitunes.ui.MainActivity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
class AlbumSheetNavigationTest {
    @get:Rule val composeRule = createAndroidComposeRule<MainActivity>()

    private var requestedAlbumId: String? = null
    private val testModule = module {
        single<AlbumRepository> {
            object : AlbumRepository {
                override suspend fun loadAlbum(albumId: String): ResultWrapper<Album> {
                    requestedAlbumId = albumId
                    return ResultWrapper.Success(
                        Album(
                            id = albumId,
                            name = "Test album",
                            coverUrl = "",
                            isExplicit = false,
                            artistName = "Test artist",
                            trackList = listOf(
                                Track(
                                    "test-track",
                                    "Album track",
                                    "Test artist",
                                    "Test album",
                                    "",
                                    false
                                )
                            ),
                        )
                    )
                }
            }
        }
    }

    @Before
    fun useFixedAlbumData() {
        loadKoinModules(testModule)
        runBlocking {
            GlobalContext.get().get<SongRepository>().addToRecentlyPlayed(
                Track(
                    id = "1",
                    name = "Song 1",
                    artistName = "Artist 1",
                    albumName = "Album 1",
                    artworkUrl = "",
                    isExplicit = false,
                    artistId = "artist-1",
                    albumId = "album-1",
                )
            )
        }
    }

    @After
    fun restoreRepositories() {
        unloadKoinModules(testModule)
        loadKoinModules(repositoryModule)
    }

    @Test
    fun moreActionsOpensSheetAndViewAlbumOpensDetails() {
        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodesWithContentDescription("More actions")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onAllNodesWithContentDescription("More actions").onFirst().performClick()
        composeRule.onNodeWithText("View album").assertIsDisplayed()
        composeRule.onNodeWithText("View album").performClick()
        assertAlbumDetailsDisplayed()
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
        assertAlbumDetailsDisplayed()

        composeRule.onNodeWithContentDescription("Back").performClick()

        composeRule.onNodeWithText("Song 1").assertIsDisplayed()
    }

    @Test
    fun albumTrackOpensSongPageAndItsAlbumMenu() {
        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodesWithContentDescription("More actions")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onAllNodesWithContentDescription("More actions").onFirst().performClick()
        composeRule.onNodeWithText("View album").performClick()
        assertAlbumDetailsDisplayed()

        composeRule.onNodeWithText("Album track").performClick()
        composeRule.onAllNodesWithText("Album track").onFirst().assertIsDisplayed()
        composeRule.onNodeWithContentDescription("More options").performClick()
        composeRule.onNodeWithText("View album").assertIsDisplayed()
    }

    private fun assertAlbumDetailsDisplayed() {
        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodes(androidx.compose.ui.test.hasText("Test album"))
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onAllNodesWithText("Test album").onFirst().assertIsDisplayed()
        composeRule.onNodeWithText("Album track").assertIsDisplayed()
        assertEquals("album-1", requestedAlbumId)
    }
}
