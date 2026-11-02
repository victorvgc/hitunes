package com.skilltest.internal

import com.skilltest.domain.model.Album
import com.skilltest.domain.model.ResultWrapper
import com.skilltest.domain.repository.AlbumRepository
import com.skilltest.domain.usecase.LoadAlbumDetailsUseCase
import com.skilltest.internal.ui.viewmodel.AlbumComponentEffect
import com.skilltest.internal.ui.viewmodel.AlbumComponentIntent
import com.skilltest.internal.ui.viewmodel.AlbumComponentState
import com.skilltest.internal.ui.viewmodel.AlbumReducer
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test

class AlbumReducerTest {
    private val album = Album.empty().copy(id = "album-8", name = "Thriller")
    private val repository = FakeAlbumRepository(album)
    private val reducer = AlbumReducer(LoadAlbumDetailsUseCase(repository))

    @Test
    fun viewAlbumEmitsNavigationEffectWithAlbumAndSongDetails() = runBlocking {
        val reduction = reducer.reduce(
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
        assertEquals(
            listOf(
                AlbumComponentIntent.LoadingDetails,
                AlbumComponentIntent.LoadAlbumDetails("album-8"),
            ),
            reduction.derivedIntents,
        )
    }

    @Test
    fun loadingAlbumUpdatesStateAndDelegatesToUseCase() = runBlocking {
        val loading = reducer.reduce(
            AlbumComponentState(),
            AlbumComponentIntent.LoadingDetails,
        ).partialStateChange.reduce(AlbumComponentState())
        assertTrue(loading.isLoading)

        val loaded = reducer.reduce(
            loading,
            AlbumComponentIntent.LoadAlbumDetails("album-8"),
        ).partialStateChange.reduce(loading)

        assertEquals("album-8", repository.requestedId)
        assertEquals(album, loaded.album)
        assertFalse(loaded.isLoading)
    }

    private class FakeAlbumRepository(private val album: Album) : AlbumRepository {
        var requestedId: String? = null
        override suspend fun loadAlbum(albumId: String): ResultWrapper<Album> {
            requestedId = albumId
            return ResultWrapper.Success(album)
        }
    }
}
