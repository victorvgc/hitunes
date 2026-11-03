package com.skilltest.data.remote.model

import com.skilltest.domain.model.Track

data class RemoteEntity(
    val wrapperType: String? = null,
    val collectionType: String? = null,
    val kind: String? = null,
    val artistId: Long? = null,
    val collectionId: Long? = null,
    val trackId: Long? = null,
    val trackName: String? = null,
    val artistName: String? = null,
    val collectionName: String? = null,
    val collectionCensoredName: String? = null,
    val trackCensoredName: String? = null,
    val previewUrl: String? = null,
    val artworkUrl100: String? = null,
    val trackExplicitness: String? = null,
    val collectionExplicitness: String? = null,
    val isStreamable: Boolean? = null,
) {
    companion object {
        private const val WRAPPER_TRACK = "track"
        private const val NOT_EXPLICIT_VALUE = "notExplicit"
    }

    fun toTrackDomain(): Track? {
        if (this.wrapperType.equals(WRAPPER_TRACK, ignoreCase = true).not()) return null

        val isExplicit = trackExplicitness?.equals(NOT_EXPLICIT_VALUE, ignoreCase = true) == false
        val isAlbumExplicit =
            collectionExplicitness?.equals(NOT_EXPLICIT_VALUE, ignoreCase = true) == false

        return Track(
            id = trackId?.toString().orEmpty(),
            name = (if (isExplicit) trackCensoredName else trackName).orEmpty(),
            artistName = artistName.orEmpty(),
            albumName = (if (isAlbumExplicit) collectionCensoredName else collectionName).orEmpty(),
            artworkUrl = artworkUrl100.orEmpty(),
            isExplicit = isExplicit,
            artistId = artistId?.toString().orEmpty(),
            albumId = collectionId?.toString().orEmpty(),
            streamUrl = previewUrl.orEmpty(),
        )
    }
}
