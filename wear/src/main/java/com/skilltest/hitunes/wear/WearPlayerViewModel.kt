package com.skilltest.hitunes.wear

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataItem
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.Wearable
import com.skilltest.core.wearsync.NowPlaying
import com.skilltest.core.wearsync.WearCommand
import com.skilltest.core.wearsync.WearSync
import com.skilltest.core.wearsync.toNowPlaying
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber

// Mirrors what the phone is playing; the phone's PlaybackService publishes it.
class WearPlayerViewModel(application: Application) : AndroidViewModel(application),
    DataClient.OnDataChangedListener {

    private val dataClient = Wearable.getDataClient(application)
    private val messageClient = Wearable.getMessageClient(application)
    private val nodeClient = Wearable.getNodeClient(application)

    private val _nowPlaying = MutableStateFlow<NowPlaying?>(null)
    val nowPlaying: StateFlow<NowPlaying?> = _nowPlaying.asStateFlow()

    init {
        dataClient.addListener(this)
        viewModelScope.launch {
            runCatching {
                val items = dataClient.dataItems.await()
                try {
                    items.firstOrNull { it.isNowPlaying() }?.let { update(it) }
                } finally {
                    items.release()
                }
            }.onFailure { Timber.w(it, "Could not read the phone's playback state") }
        }
    }

    override fun onDataChanged(events: DataEventBuffer) {
        events.forEach { event ->
            if (!event.dataItem.isNowPlaying()) return@forEach
            if (event.type == DataEvent.TYPE_DELETED) _nowPlaying.value = null
            else update(event.dataItem)
        }
    }

    fun send(command: WearCommand) {
        viewModelScope.launch {
            runCatching {
                nodeClient.connectedNodes.await().forEach { node ->
                    messageClient.sendMessage(node.id, WearSync.COMMAND_PATH, command.toBytes())
                        .await()
                }
            }.onFailure { Timber.w(it, "Could not send %s to the phone", command) }
        }
    }

    override fun onCleared() {
        dataClient.removeListener(this)
        super.onCleared()
    }

    private fun update(item: DataItem) {
        _nowPlaying.value = DataMapItem.fromDataItem(item).dataMap.toNowPlaying()
    }

    private fun DataItem.isNowPlaying() = uri.path == WearSync.NOW_PLAYING_PATH
}
