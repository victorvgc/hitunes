package com.skilltest.songplayer.internal.viewmodel

import com.skilltest.core.presentation.viewmodel.BaseUiState
import com.skilltest.core.presentation.viewmodel.PartialStateChange
import com.skilltest.domain.model.Track

data class PlayerState(
    val track: Track? = null,
    val isRepeating: Boolean = false,
    val isPlaying: Boolean = false,
    val positionMs: Long = 0,
    val durationMs: Long = 0,
    val hasNext: Boolean = false,
    val hasPrevious: Boolean = false,
) : BaseUiState("PlayerState")

internal data class PlayerStateChange(val state: PlayerState) : PartialStateChange<PlayerState> {
    override fun reduce(previous: PlayerState): PlayerState = state
}
