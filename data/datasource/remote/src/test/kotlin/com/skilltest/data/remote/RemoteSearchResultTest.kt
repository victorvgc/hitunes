package com.skilltest.data.remote

import com.skilltest.data.remote.model.RemoteSearchResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RemoteSearchResultTest {
    @Test
    fun mapsIdsNamesAndArtworkForExplicitTrack() {
        val track = remoteTrack("explicit").toDomain()

        assertEquals("42", track.id)
        assertEquals("7", track.artistId)
        assertEquals("9", track.albumId)
        assertEquals("Censored track", track.name)
        assertEquals("Censored album", track.albumName)
        assertEquals("Artist", track.artistName)
        assertEquals("https://art.test/cover", track.artworkUrl)
        assertTrue(track.isExplicit)
    }

    @Test
    fun keepsOriginalNamesWhenNotExplicitIgnoringCase() {
        val track = remoteTrack("NoTeXpLiCiT").toDomain()

        assertEquals("Original track", track.name)
        assertEquals("Original album", track.albumName)
        assertFalse(track.isExplicit)
    }

    @Test
    fun mapsEverySearchResultInOrder() {
        val result = RemoteSearchResult(
            resultCount = 2,
            results = listOf(remoteTrack("explicit"), remoteTrack("notExplicit").copy(trackId = 43)),
        ).toDomain()

        assertEquals(listOf("42", "43"), result.trackList.map { it.id })
    }

    private fun remoteTrack(explicitness: String) = RemoteSearchResult.RemoteTrack(
        artistId = 7,
        collectionId = 9,
        trackId = 42,
        trackName = "Original track",
        artistName = "Artist",
        collectionName = "Original album",
        collectionCensoredName = "Censored album",
        trackCensoredName = "Censored track",
        previewUrl = "https://art.test/preview",
        artworkUrl100 = "https://art.test/cover",
        trackExplicitness = explicitness,
        collectionExplicitness = explicitness,
        isStreamable = true,
    )
}
