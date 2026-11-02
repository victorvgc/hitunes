package com.skilltest.data.remote

import com.google.gson.Gson
import com.skilltest.data.remote.model.RemoteAlbum
import com.skilltest.data.remote.model.RemoteEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RemoteAlbumTest {
    @Test
    fun missingAlbumFieldsMapToEmptyDomainValues() {
        val album = RemoteAlbum().toDomain()

        assertEquals("", album.id)
        assertEquals("", album.name)
        assertFalse(album.isExplicit)
        assertTrue(album.trackList.isEmpty())
    }

    @Test
    fun collectionAndTrackEntitiesMapToAlbumAndSongs() {
        val album = RemoteAlbum(
            results = listOf(
                null,
                RemoteEntity(
                    wrapperType = "collection",
                    collectionId = 9,
                    collectionName = "Original album",
                    collectionCensoredName = "Censored album",
                    collectionExplicitness = "explicit",
                ),
                RemoteEntity(
                    wrapperType = "track",
                    trackId = 42,
                    trackName = "Original song",
                    artistName = "Artist",
                    collectionId = 9,
                    artistId = 7,
                    trackExplicitness = "notExplicit",
                ),
            ),
        ).toDomain()

        assertEquals("9", album.id)
        assertEquals("Censored album", album.name)
        assertTrue(album.isExplicit)
        assertEquals(1, album.trackList.size)
        assertEquals("42", album.trackList.single().id)
        assertEquals("7", album.trackList.single().artistId)
        assertEquals("Original song", album.trackList.single().name)
    }

    @Test
    fun remoteEntityMissingFieldsMapToEmptyTrack() {
        val track = RemoteEntity().toDomain()

        assertEquals("", track.id)
        assertEquals("", track.name)
        assertEquals("", track.artistName)
        assertEquals("", track.albumName)
        assertEquals("", track.artworkUrl)
        assertEquals("", track.artistId)
        assertEquals("", track.albumId)
        assertFalse(track.isExplicit)
    }

    @Test
    fun omittedAlbumJsonFieldsMapToEmptyDomainValues() {
        val remote = Gson().fromJson(
            """{"results":[{"wrapperType":"collection","collectionId":9}]}""",
            RemoteAlbum::class.java,
        )

        val album = remote.toDomain()
        assertEquals("9", album.id)
        assertEquals("", album.name)
        assertTrue(album.trackList.isEmpty())
    }

    @Test
    fun trackOnlyResponseDoesNotUseTrackAsAlbumMetadata() {
        val album = RemoteAlbum(
            results = listOf(RemoteEntity(wrapperType = "track", trackId = 42, collectionId = 9)),
        ).toDomain()

        assertEquals("", album.id)
        assertEquals("", album.name)
        assertEquals(listOf("42"), album.trackList.map { it.id })
    }

    @Test
    fun suppliedAlbumLookupMapsOnlyTrackWrappers() {
        val json = requireNotNull(javaClass.getResourceAsStream("/album_lookup.json"))
            .bufferedReader().use { it.readText() }
        val remote = Gson().fromJson(json, RemoteAlbum::class.java)

        val album = remote.toDomain()

        assertEquals("269572838", album.id)
        assertEquals("Thriller", album.name)
        assertFalse(album.isExplicit)
        assertEquals(9, album.trackList.size)
        assertEquals("269572927", album.trackList.first().id)
        assertEquals("Wanna Be Startin' Somethin'", album.trackList.first().name)
        assertTrue(album.trackList.none { it.id.isEmpty() })
    }
}
