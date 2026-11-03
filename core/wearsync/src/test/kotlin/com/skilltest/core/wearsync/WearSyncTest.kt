package com.skilltest.core.wearsync

import com.google.android.gms.wearable.DataMap
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class WearSyncTest {
    @Test
    fun `commands round trip through message bytes`() {
        WearCommand.entries.forEach { command ->
            assertEquals(command, WearCommand.fromBytes(command.toBytes()))
        }
    }

    @Test
    fun `unknown or missing command bytes are ignored`() {
        assertNull(WearCommand.fromBytes("Rewind".toByteArray()))
        assertNull(WearCommand.fromBytes(null))
    }

    @Test
    fun `now playing round trips through a data map`() {
        val nowPlaying = NowPlaying(
            title = "Thriller",
            artist = "Michael Jackson",
            artworkUrl = "https://example.test/art.jpg",
            isPlaying = true,
            hasNext = true,
            hasPrevious = false,
        )
        val dataMap = DataMap()

        nowPlaying.writeTo(dataMap)

        assertEquals(nowPlaying, dataMap.toNowPlaying())
    }

    @Test
    fun `nothing playing reads back as no track`() {
        val dataMap = DataMap()

        (null as NowPlaying?).writeTo(dataMap)

        assertNull(dataMap.toNowPlaying())
    }
}
