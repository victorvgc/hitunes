package com.skilltest.songplayer.internal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.unit.dp
import com.skilltest.design.theme.HiTunesTheme
import com.skilltest.domain.model.Track
import com.skilltest.songplayer.internal.ui.SongDetailsContent
import com.skilltest.songplayer.internal.viewmodel.PlayerState
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], qualifiers = "w411dp-h891dp")
class SongDetailsCoverAlignmentTest {
    @get:Rule
    val composeRule = createComposeRule()

    // Opened from the mini player, the cover runs a shared-element transition; it must stay
    // horizontally centered like the static layout.
    @OptIn(ExperimentalSharedTransitionApi::class)
    @Test
    fun coverStaysCenteredWithSharedTransition() {
        composeRule.setContent {
            HiTunesTheme(darkTheme = false) {
                SharedTransitionLayout {
                    AnimatedVisibility(visible = true) {
                        Box(modifier = Modifier.width(411.dp)) {
                            SongDetailsContent(
                                track = track,
                                state = PlayerState(track = track),
                                onIntent = {},
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedVisibilityScope = this@AnimatedVisibility,
                            )
                        }
                    }
                }
            }
        }

        val bounds = composeRule.onNodeWithContentDescription(track.name).getUnclippedBoundsInRoot()
        val center = (bounds.left + bounds.right) / 2
        assertEquals(411f / 2, center.value, 1f)
    }

    private val track = Track(
        id = "track-1",
        name = "Thriller",
        artistName = "Michael Jackson",
        albumName = "Thriller",
        artworkUrl = "",
        isExplicit = false,
    )
}
