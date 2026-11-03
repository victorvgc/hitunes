package com.skilltest.core.wearsync

import com.google.android.gms.wearable.DataMap
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.PutDataRequest

// Phone and watch share these over the Wearable Data Layer. Both apps must keep the same
// application id and signing key for the data layer to pair them.
object WearSync {
    const val NOW_PLAYING_PATH = "/hitunes/now-playing"
    const val COMMAND_PATH = "/hitunes/command"
}

data class NowPlaying(
    val title: String,
    val artist: String,
    val artworkUrl: String,
    val isPlaying: Boolean,
    val hasNext: Boolean,
    val hasPrevious: Boolean,
)

enum class WearCommand {
    Play, Pause, Next, Previous;

    fun toBytes(): ByteArray = name.toByteArray()

    companion object {
        fun fromBytes(bytes: ByteArray?): WearCommand? =
            bytes?.let { data -> entries.firstOrNull { it.name == String(data) } }
    }
}

private const val KEY_HAS_TRACK = "has_track"
private const val KEY_TITLE = "title"
private const val KEY_ARTIST = "artist"
private const val KEY_ARTWORK_URL = "artwork_url"
private const val KEY_IS_PLAYING = "is_playing"
private const val KEY_HAS_NEXT = "has_next"
private const val KEY_HAS_PREVIOUS = "has_previous"

// Nothing playing is sent as an item without a track, so the watch can tell it apart from
// "the phone never sent anything".
fun NowPlaying?.toPutDataRequest(): PutDataRequest =
    PutDataMapRequest.create(WearSync.NOW_PLAYING_PATH).apply {
        writeTo(dataMap)
    }.asPutDataRequest().setUrgent()

fun NowPlaying?.writeTo(dataMap: DataMap) {
    dataMap.putBoolean(KEY_HAS_TRACK, this != null)
    if (this == null) return
    dataMap.putString(KEY_TITLE, title)
    dataMap.putString(KEY_ARTIST, artist)
    dataMap.putString(KEY_ARTWORK_URL, artworkUrl)
    dataMap.putBoolean(KEY_IS_PLAYING, isPlaying)
    dataMap.putBoolean(KEY_HAS_NEXT, hasNext)
    dataMap.putBoolean(KEY_HAS_PREVIOUS, hasPrevious)
}

fun DataMap.toNowPlaying(): NowPlaying? {
    if (!getBoolean(KEY_HAS_TRACK)) return null
    return NowPlaying(
        title = getString(KEY_TITLE).orEmpty(),
        artist = getString(KEY_ARTIST).orEmpty(),
        artworkUrl = getString(KEY_ARTWORK_URL).orEmpty(),
        isPlaying = getBoolean(KEY_IS_PLAYING),
        hasNext = getBoolean(KEY_HAS_NEXT),
        hasPrevious = getBoolean(KEY_HAS_PREVIOUS),
    )
}
