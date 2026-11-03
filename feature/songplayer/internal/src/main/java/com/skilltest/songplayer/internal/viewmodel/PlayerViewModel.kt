package com.skilltest.songplayer.internal.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.skilltest.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class PlayerViewModel(
    private val playerReducer: PlayerReducer,
) : BaseViewModel<PlayerState, PlayerIntent, PlayerEffect>(
    initialState = PlayerState(),
    reducer = playerReducer,
) {
    private var controller: MediaController? = null
    private var progressJob: Job? = null
    private val listener = object : Player.Listener {
        override fun onEvents(player: Player, events: Player.Events) {
            onIntent(PlayerIntent.SyncPlayback)
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            playerReducer.trackForMediaId(mediaItem?.mediaId)?.let { track ->
                onIntent(PlayerIntent.TrackStarted(track))
            }
        }
    }

    fun bind(controller: MediaController?) {
        if (this.controller === controller) return
        this.controller?.removeListener(listener)
        progressJob?.cancel()
        this.controller = controller
        controller?.addListener(listener)
        playerReducer.bind(controller)
        onIntent(PlayerIntent.SyncPlayback)
        if (controller != null) {
            progressJob = viewModelScope.launch {
                while (isActive) {
                    if (controller.isPlaying) onIntent(PlayerIntent.SyncPlayback)
                    delay(500.milliseconds)
                }
            }
        }
    }

    override fun onCleared() {
        progressJob?.cancel()
        controller?.removeListener(listener)
        playerReducer.bind(null)
        super.onCleared()
    }
}
