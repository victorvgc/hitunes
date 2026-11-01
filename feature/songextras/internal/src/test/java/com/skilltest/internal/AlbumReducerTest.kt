package com.skilltest.internal

import com.skilltest.internal.ui.viewmodel.AlbumComponentEffect
import com.skilltest.internal.ui.viewmodel.AlbumComponentIntent
import com.skilltest.internal.ui.viewmodel.AlbumComponentState
import com.skilltest.internal.ui.viewmodel.AlbumReducer
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AlbumReducerTest {
    @Test
    fun viewAlbumEmitsNavigationEffectWithAlbumAndSongDetails() = runBlocking {
        val reduction = AlbumReducer().reduce(
            AlbumComponentState(),
            AlbumComponentIntent.OpenAlbumDetails(
                albumId = "album-8",
                trackId = "track-5",
                trackName = "Thriller",
                artistId = "artist-7",
                artistName = "Michael Jackson",
            ),
        )

        assertEquals(
            AlbumComponentEffect.NavigateToAlbumDetails(
                albumId = "album-8",
                trackId = "track-5",
                trackName = "Thriller",
                artistId = "artist-7",
                artistName = "Michael Jackson",
            ),
            reduction.effects.single(),
        )
        assertTrue(reduction.derivedIntents.isEmpty())
    }
}
