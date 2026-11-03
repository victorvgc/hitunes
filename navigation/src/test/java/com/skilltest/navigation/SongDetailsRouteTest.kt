package com.skilltest.navigation

import com.skilltest.domain.model.Track
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class SongDetailsRouteTest {
    @Test
    fun songDetailsRetainsTheSelectedTrackAcrossSerialization() {
        val track = Track(
            id = "42",
            name = "Song",
            artistName = "Artist",
            albumName = "Album",
            artworkUrl = "https://example.test/art.jpg",
            isExplicit = false,
            streamUrl = "https://example.test/stream.mp3",
        )
        val route = Route.SongDetails.from(track)
        val restored = Json.decodeFromString<Route>(Json.encodeToString<Route>(route))

        assertEquals(track, route.toTrack())
        assertEquals(track, SongNavigationTrack.decode(route.track).toTrack())
        assertEquals(route, restored)
    }
}
