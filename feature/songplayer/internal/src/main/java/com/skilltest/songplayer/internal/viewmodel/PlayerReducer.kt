package com.skilltest.songplayer.internal.viewmodel

import android.net.Uri
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.skilltest.core.presentation.viewmodel.BaseReducer
import com.skilltest.core.presentation.viewmodel.Reduction
import com.skilltest.domain.model.Track
import com.skilltest.domain.usecase.AddToRecentlyPlayedUseCase

class PlayerReducer(
    private val addToRecentlyPlayedUseCase: AddToRecentlyPlayedUseCase,
) : BaseReducer<PlayerState, PlayerIntent, PlayerEffect> {
    private var controller: MediaController? = null
    private var pendingTrack: PlayerIntent.PlayTrack? = null
    private var resetRepeatOnBind = false
    private var currentQueue: List<Track> = emptyList()

    fun bind(controller: MediaController?) {
        this.controller = controller
        if (controller != null) {
            if (resetRepeatOnBind) {
                controller.repeatMode = Player.REPEAT_MODE_OFF
                resetRepeatOnBind = false
            }
            pendingTrack?.let {
                pendingTrack = null
                playTrack(controller, it)
            }
        }
    }

    fun trackForMediaId(id: String?): Track? = currentQueue.firstOrNull { it.id == id }

    override suspend fun reduce(
        previousState: PlayerState,
        intent: PlayerIntent,
    ): Reduction<PlayerState, PlayerIntent, PlayerEffect> {
        if (intent is PlayerIntent.TrackStarted) {
            addToRecentlyPlayedUseCase(AddToRecentlyPlayedUseCase.Request(intent.track))
            return Reduction.none()
        }

        val player = controller
        val fallback = when (intent) {
            is PlayerIntent.PlayTrack -> {
                if (player == null) pendingTrack = intent else playTrack(player, intent)
                previousState.copy(track = intent.track)
            }

            PlayerIntent.Play -> {
                player?.play(); previousState
            }

            PlayerIntent.Pause -> {
                player?.pause(); previousState
            }

            PlayerIntent.Next -> {
                if (player?.hasNextMediaItem() == true) player.seekToNextMediaItem()
                previousState
            }

            PlayerIntent.Previous -> {
                if (player?.hasPreviousMediaItem() == true) player.seekToPreviousMediaItem()
                previousState
            }

            PlayerIntent.ToggleOnRepeat -> {
                player?.let {
                    it.repeatMode = toggledRepeatMode(it.repeatMode)
                }
                previousState
            }

            PlayerIntent.DisableRepeat -> {
                if (player == null) resetRepeatOnBind = true
                else player.repeatMode = Player.REPEAT_MODE_OFF
                previousState.copy(isRepeating = false)
            }

            PlayerIntent.Forward -> {
                player?.let {
                    val end = if (it.duration == C.TIME_UNSET) Long.MAX_VALUE else it.duration
                    it.seekTo((it.currentPosition + 10_000).coerceAtMost(end))
                }
                previousState
            }

            PlayerIntent.Backward -> {
                player?.seekTo((player.currentPosition - 10_000).coerceAtLeast(0))
                previousState
            }

            is PlayerIntent.SeekTo -> {
                player?.seekTo(intent.positionMs.coerceAtLeast(0))
                previousState
            }

            PlayerIntent.SyncPlayback -> previousState
            is PlayerIntent.TrackStarted -> previousState
        }
        val nextState = snapshot(fallback)
        return Reduction.state(
            PlayerStateChange(
                if (intent == PlayerIntent.DisableRepeat) nextState.copy(isRepeating = false)
                else nextState
            )
        )
    }

    private fun playTrack(player: MediaController, request: PlayerIntent.PlayTrack) {
        val track = request.track
        if (track.streamUrl.isBlank()) return
        val queue = request.queue.filter { it.streamUrl.isNotBlank() }.ifEmpty { listOf(track) }
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

    private fun snapshot(previous: PlayerState): PlayerState {
        val player = controller ?: return previous
        val item = player.currentMediaItem
        val track = when {
            item == null -> previous.track
            previous.track?.id == item.mediaId -> previous.track
            else -> trackForMediaId(item.mediaId) ?: Track(
                id = item.mediaId,
                name = item.mediaMetadata.title?.toString().orEmpty(),
                artistName = item.mediaMetadata.artist?.toString().orEmpty(),
                albumName = "",
                artworkUrl = item.mediaMetadata.artworkUri?.toString().orEmpty(),
                isExplicit = false,
                streamUrl = item.localConfiguration?.uri?.toString().orEmpty(),
            )
        }
        return PlayerState(
            track = track,
            isRepeating = isCurrentTrackRepeating(player.repeatMode),
            isPlaying = player.isPlaying,
            positionMs = player.currentPosition.coerceAtLeast(0),
            durationMs = player.duration.takeIf { it != C.TIME_UNSET && it > 0 } ?: 0,
            hasNext = player.hasNextMediaItem(),
            hasPrevious = player.hasPreviousMediaItem(),
        )
    }
}

internal fun toggledRepeatMode(currentMode: Int): Int =
    if (currentMode == Player.REPEAT_MODE_ONE) Player.REPEAT_MODE_OFF else Player.REPEAT_MODE_ONE

internal fun isCurrentTrackRepeating(mode: Int): Boolean = mode == Player.REPEAT_MODE_ONE
