package com.skilltest.data.remote

import com.google.gson.Gson
import com.skilltest.data.remote.model.RemoteEntity
import com.skilltest.data.remote.model.RemoteSearchResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RemoteSearchResultTest {
    @Test
    fun mapsIdsNamesAndArtworkForExplicitTrack() {
        val track = requireNotNull(remoteTrack("explicit").toTrackDomain())

        assertEquals("42", track.id)
        assertEquals("7", track.artistId)
        assertEquals("9", track.albumId)
        assertEquals("Censored track", track.name)
        assertEquals("Censored album", track.albumName)
        assertEquals("Artist", track.artistName)
        assertEquals("https://art.test/cover", track.artworkUrl)
        assertEquals("https://art.test/preview", track.streamUrl)
        assertTrue(track.isExplicit)
    }

    @Test
    fun keepsOriginalNamesWhenNotExplicitIgnoringCase() {
        val track = requireNotNull(remoteTrack("NoTeXpLiCiT").toTrackDomain())

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

    @Test
    fun skipsNonTrackWrappersAndAcceptsTrackWrappersIgnoringCase() {
        val result = RemoteSearchResult(
            results = listOf(
                RemoteEntity(),
                RemoteEntity(wrapperType = "collection", collectionId = 9),
                remoteTrack("notExplicit").copy(wrapperType = "TrAcK"),
            ),
        ).toDomain()

        assertEquals(listOf("42"), result.trackList.map { it.id })
    }

    @Test
    fun missingTrackFieldsMapToEmptyValuesAndFalse() {
        val track = requireNotNull(RemoteEntity(wrapperType = "track").toTrackDomain())

        assertEquals("", track.id)
        assertEquals("", track.artistId)
        assertEquals("", track.albumId)
        assertEquals("", track.name)
        assertEquals("", track.artistName)
        assertEquals("", track.albumName)
        assertEquals("", track.artworkUrl)
        assertFalse(track.isExplicit)
    }

    @Test
    fun missingResultsMapToEmptyListAndNullEntriesAreSkipped() {
        assertTrue(RemoteSearchResult().toDomain().trackList.isEmpty())

        val result = RemoteSearchResult(results = listOf(null, RemoteEntity(wrapperType = "track")))
        assertEquals(1, result.toDomain().trackList.size)
    }

    @Test
    fun collectionExplicitnessControlsAlbumNameIndependently() {
        val track = requireNotNull(
            remoteTrack("notExplicit")
                .copy(collectionExplicitness = "explicit")
                .toTrackDomain()
        )

        assertFalse(track.isExplicit)
        assertEquals("Censored album", track.albumName)
    }

    @Test
    fun omittedJsonFieldsMapToEmptyDomainValues() {
        val remote = Gson().fromJson(
            """{"results":[{"wrapperType":"track","trackId":42},null]}""",
            RemoteSearchResult::class.java,
        )

        val track = remote.toDomain().trackList.single()
        assertEquals("42", track.id)
        assertEquals("", track.name)
        assertEquals("", track.artistId)
        assertFalse(track.isExplicit)
    }

    private fun remoteTrack(explicitness: String) = RemoteEntity(
        wrapperType = "track",
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
