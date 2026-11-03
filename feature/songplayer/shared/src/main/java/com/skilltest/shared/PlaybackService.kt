package com.skilltest.shared

import android.content.Intent
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.skilltest.domain.model.Track
import com.skilltest.domain.usecase.GetRecentlyPlayedUseCase
import com.skilltest.songplayer.internal.media.toMediaItem
import com.skilltest.songplayer.shared.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

// A library service (not just a session service) so Android Auto and Android Automotive can
// browse the recently played songs; their own media UI draws the screen.
class PlaybackService : MediaLibraryService() {

    private val getRecentlyPlayed: GetRecentlyPlayedUseCase by inject()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private var mediaSession: MediaLibrarySession? = null
    private var wearBridge: WearPlaybackBridge? = null
    private var recentlyPlayed: List<Track> = emptyList()

    override fun onCreate() {
        super.onCreate()

        val player = ExoPlayer.Builder(this).build()

        val session = MediaLibrarySession.Builder(this, player, LibraryCallback()).build()
        mediaSession = session

        wearBridge = WearPlaybackBridge(this, player).also { it.start() }

        scope.launch {
            getRecentlyPlayed(Unit).collect { result ->
                recentlyPlayed = result.data?.trackList.orEmpty()
                    .filter { it.streamUrl.isNotBlank() }
                session.notifyChildrenChanged(ROOT_ID, recentlyPlayed.size, null)
            }
        }
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? {
        return mediaSession
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)

        val player = mediaSession?.player

        if (player != null && (player.playWhenReady.not() || player.mediaItemCount == 0)) {
            stopSelf()
        }
    }

    override fun onDestroy() {
        scope.cancel()
        wearBridge?.stop()
        wearBridge = null

        mediaSession?.run {
            player.release()

            release()

            mediaSession = null
        }

        super.onDestroy()
    }

    private inner class LibraryCallback : MediaLibrarySession.Callback {
        override fun onGetLibraryRoot(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            params: LibraryParams?,
        ): ListenableFuture<LibraryResult<MediaItem>> =
            Futures.immediateFuture(LibraryResult.ofItem(rootItem(), params))

        override fun onGetChildren(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            parentId: String,
            page: Int,
            pageSize: Int,
            params: LibraryParams?,
        ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
            val children = if (parentId == ROOT_ID) {
                recentlyPlayed.drop(page * pageSize).take(pageSize).map { it.toMediaItem() }
            } else {
                emptyList()
            }
            return Futures.immediateFuture(
                LibraryResult.ofItemList(ImmutableList.copyOf(children), params)
            )
        }

        override fun onGetItem(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            mediaId: String,
        ): ListenableFuture<LibraryResult<MediaItem>> {
            val item = if (mediaId == ROOT_ID) rootItem()
            else recentlyPlayed.firstOrNull { it.id == mediaId }?.toMediaItem()
            return Futures.immediateFuture(
                if (item != null) LibraryResult.ofItem(item, null)
                else LibraryResult.ofError(LibraryResult.RESULT_ERROR_BAD_VALUE)
            )
        }

        // The car only sends media ids; the phone app already sends playable items.
        override fun onAddMediaItems(
            mediaSession: MediaSession,
            controller: MediaSession.ControllerInfo,
            mediaItems: MutableList<MediaItem>,
        ): ListenableFuture<MutableList<MediaItem>> = Futures.immediateFuture(
            mediaItems.mapNotNull { item ->
                if (item.localConfiguration != null) item
                else recentlyPlayed.firstOrNull { it.id == item.mediaId }?.toMediaItem()
            }.toMutableList()
        )
    }

    private fun rootItem(): MediaItem = MediaItem.Builder()
        .setMediaId(ROOT_ID)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle(getString(R.string.library_recently_played))
                .setIsBrowsable(true)
                .setIsPlayable(false)
                .setMediaType(MediaMetadata.MEDIA_TYPE_FOLDER_MIXED)
                .build()
        )
        .build()

    private companion object {
        const val ROOT_ID = "recently-played"
    }
}
