package com.skilltest.songplayer.internal.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.skilltest.domain.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class PlayerState(
    val track: Track? = null,
    val isPlaying: Boolean = false,
    val positionMs: Long = 0,
    val durationMs: Long = 0,
    val hasNext: Boolean = false,
    val hasPrevious: Boolean = false,
)

sealed interface PlayerIntent {
    data class PlayTrack(val track: Track, val queue: List<Track> = listOf(track)) : PlayerIntent
    data object Play : PlayerIntent
    data object Pause : PlayerIntent
    data object Next : PlayerIntent
    data object Previous : PlayerIntent
    data object Forward : PlayerIntent
    data object Backward : PlayerIntent
    data class SeekTo(val positionMs: Long) : PlayerIntent
}

class PlayerViewModel : ViewModel() {
    private val mutableState = MutableStateFlow(PlayerState())
    val state = mutableState.asStateFlow()

    private var controller: MediaController? = null
    private var pendingTrack: PlayerIntent.PlayTrack? = null
    private var currentQueue: List<Track> = emptyList()
    private var progressJob: Job? = null
    private val listener = object : Player.Listener {
        override fun onEvents(player: Player, events: Player.Events) = refreshState()
    }

    fun bind(controller: MediaController?) {
        if (this.controller === controller) return
        this.controller?.removeListener(listener)
        progressJob?.cancel()
        this.controller = controller
        controller?.addListener(listener)
        pendingTrack?.let { request ->
            if (controller != null) {
                pendingTrack = null
                playTrack(controller, request)
            }
        }
        refreshState()
        if (controller != null) {
            progressJob = viewModelScope.launch {
                while (isActive) {
                    if (controller.isPlaying) refreshState()
                    delay(500)
                }
            }
        }
    }

    fun onIntent(intent: PlayerIntent) {
        val player = controller
        when (intent) {
            is PlayerIntent.PlayTrack -> {
                mutableState.value = mutableState.value.copy(track = intent.track)
                if (player == null) pendingTrack = intent else playTrack(player, intent)
            }

            PlayerIntent.Play -> player?.play()
            PlayerIntent.Pause -> player?.pause()
            PlayerIntent.Next -> if (player?.hasNextMediaItem() == true) player.seekToNextMediaItem()
            PlayerIntent.Previous -> if (player?.hasPreviousMediaItem() == true) player.seekToPreviousMediaItem()
            PlayerIntent.Forward -> player?.let {
                val end = if (it.duration == C.TIME_UNSET) Long.MAX_VALUE else it.duration
                it.seekTo((it.currentPosition + 10_000).coerceAtMost(end))
            }

            PlayerIntent.Backward -> player?.seekTo(
                (player.currentPosition - 10_000).coerceAtLeast(
                    0
                )
            )

            is PlayerIntent.SeekTo -> player?.seekTo(intent.positionMs.coerceAtLeast(0))
        }
        refreshState()
    }

    private fun playTrack(player: MediaController, request: PlayerIntent.PlayTrack) {
        val track = request.track
        if (track.streamUrl.isBlank()) return
        val queue = request.queue.filter { it.streamUrl.isNotBlank() }
            .ifEmpty { listOf(track) }
        currentQueue = queue
        val items = queue.map { song ->
            val metadata = MediaMetadata.Builder()
                .setTitle(song.name)
                .setArtist(song.artistName)
                .apply {
                    if (song.artworkUrl.isNotBlank()) setArtworkUri(Uri.parse(song.artworkUrl))
                }
                .build()
            MediaItem.Builder()
                .setMediaId(song.id)
                .setUri(song.streamUrl)
                .setMediaMetadata(metadata)
                .build()
        }
        player.setMediaItems(items, queue.indexOfFirst { it.id == track.id }.coerceAtLeast(0), 0L)
        player.prepare()
        player.play()
    }

    private fun refreshState() {
        val player = controller ?: return
        val item = player.currentMediaItem
        val previous = mutableState.value.track
        val track = when {
            item == null -> previous
            previous?.id == item.mediaId -> previous
            else -> currentQueue.firstOrNull { it.id == item.mediaId } ?: Track(
                id = item.mediaId,
                name = item.mediaMetadata.title?.toString().orEmpty(),
                artistName = item.mediaMetadata.artist?.toString().orEmpty(),
                albumName = "",
                artworkUrl = item.mediaMetadata.artworkUri?.toString().orEmpty(),
                isExplicit = false,
                streamUrl = item.localConfiguration?.uri?.toString().orEmpty(),
            )
        }
        mutableState.value = PlayerState(
            track = track,
            isPlaying = player.isPlaying,
            positionMs = player.currentPosition.coerceAtLeast(0),
            durationMs = player.duration.takeIf { it != C.TIME_UNSET && it > 0 } ?: 0,
            hasNext = player.hasNextMediaItem(),
            hasPrevious = player.hasPreviousMediaItem(),
        )
    }

    override fun onCleared() {
        progressJob?.cancel()
        controller?.removeListener(listener)
        super.onCleared()
    }
}
