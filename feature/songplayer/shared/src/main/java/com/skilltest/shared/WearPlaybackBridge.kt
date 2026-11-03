package com.skilltest.shared

import android.content.Context
import androidx.media3.common.Player
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.skilltest.core.wearsync.NowPlaying
import com.skilltest.core.wearsync.WearCommand
import com.skilltest.core.wearsync.WearSync
import com.skilltest.core.wearsync.toPutDataRequest
import timber.log.Timber

// Publishes the playing song to the paired watch and plays back the watch's commands.
// Lives as long as the playback service, so the watch keeps working with the app closed.
internal class WearPlaybackBridge(
    context: Context,
    private val player: Player,
) : Player.Listener, MessageClient.OnMessageReceivedListener {

    private val dataClient = Wearable.getDataClient(context)
    private val messageClient = Wearable.getMessageClient(context)
    private var published: NowPlaying? = null
    private var hasPublished = false

    fun start() {
        player.addListener(this)
        messageClient.addListener(this)
        publish(player.nowPlaying())
    }

    fun stop() {
        player.removeListener(this)
        messageClient.removeListener(this)
        publish(null)
    }

    override fun onEvents(player: Player, events: Player.Events) {
        publish(player.nowPlaying())
    }

    override fun onMessageReceived(event: MessageEvent) {
        if (event.path != WearSync.COMMAND_PATH) return
        when (WearCommand.fromBytes(event.data)) {
            WearCommand.Play -> player.play()
            WearCommand.Pause -> player.pause()
            WearCommand.Next -> if (player.hasNextMediaItem()) player.seekToNextMediaItem()
            WearCommand.Previous ->
                if (player.hasPreviousMediaItem()) player.seekToPreviousMediaItem()

            null -> Unit
        }
    }

    private fun publish(nowPlaying: NowPlaying?) {
        if (hasPublished && nowPlaying == published) return
        hasPublished = true
        published = nowPlaying
        dataClient.putDataItem(nowPlaying.toPutDataRequest())
            .addOnFailureListener { Timber.w(it, "Could not publish playback to the watch") }
    }

    private fun Player.nowPlaying(): NowPlaying? {
        val item = currentMediaItem ?: return null
        return NowPlaying(
            title = item.mediaMetadata.title?.toString().orEmpty(),
            artist = item.mediaMetadata.artist?.toString().orEmpty(),
            artworkUrl = item.mediaMetadata.artworkUri?.toString().orEmpty(),
            isPlaying = playWhenReady,
            hasNext = hasNextMediaItem(),
            hasPrevious = hasPreviousMediaItem(),
        )
    }
}
