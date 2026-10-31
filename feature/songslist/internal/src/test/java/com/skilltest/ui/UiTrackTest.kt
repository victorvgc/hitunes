package com.skilltest.ui

import com.skilltest.domain.model.Track
import com.skilltest.ui.model.UiTrack
import com.skilltest.ui.model.UiTrack.Companion.toUi
import org.junit.Assert.assertEquals
import org.junit.Test

class UiTrackTest {

    @Test
    fun `given a track when mapping to a ui model then return the model`() {
        // given
        val track = Track(
            id = "id",
            name = "test name",
            artistName = "artist",
            artworkUrl = "some_url",
            isExplicit = true,
            albumName = "album"
        )

        // when
        val result = track.toUi()

        // then
        val expected = UiTrack(
            id = "id",
            name = "test name",
            artistName = "artist",
            artwork = "some_url",
            isExplicit = true,
        )

        assertEquals(expected, result)
    }
}
