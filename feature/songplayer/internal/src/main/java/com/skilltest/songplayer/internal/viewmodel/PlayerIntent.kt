package com.skilltest.songplayer.internal.viewmodel

import com.skilltest.core.presentation.viewmodel.BaseIntent
import com.skilltest.domain.model.Track

sealed class PlayerIntent(name: String) : BaseIntent(name) {
    data class PlayTrack(val track: Track, val queue: List<Track> = listOf(track)) :
        PlayerIntent("PlayTrack")

    data object Play : PlayerIntent("Play")
    data object Pause : PlayerIntent("Pause")
    data object Next : PlayerIntent("Next")
    data object Previous : PlayerIntent("Previous")
    data object Forward : PlayerIntent("Forward")
    data object Backward : PlayerIntent("Backward")
    data object ToggleOnRepeat : PlayerIntent("ToggleOnRepeat")
    internal data object DisableRepeat : PlayerIntent("DisableRepeat")
    data class SeekTo(val positionMs: Long) : PlayerIntent("SeekTo")

    internal data object SyncPlayback : PlayerIntent("SyncPlayback")
    internal data class TrackStarted(val track: Track) : PlayerIntent("TrackStarted")
}
