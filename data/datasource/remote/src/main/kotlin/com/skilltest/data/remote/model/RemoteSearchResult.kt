package com.skilltest.data.remote.model

import com.skilltest.domain.model.SearchResult
import com.skilltest.domain.model.Track

data class RemoteSearchResult(
    val resultCount: Int,
    val results: List<RemoteTrack>
) {
    data class RemoteTrack(
        val artistId: Long,
        val collectionId: Long,
        val trackId: Long,
        val trackName: String,
        val artistName: String,
        val collectionName: String,
        val collectionCensoredName: String,
        val trackCensoredName: String,
        val previewUrl: String,
        val artworkUrl100: String,
        val trackExplicitness: String,
        val collectionExplicitness: String,
        val isStreamable: Boolean,
    ) {

        companion object {
            private const val NOT_EXPLICIT_VALUE = "notExplicit"
        }

        fun toDomain(): Track {
            val isExplicit = trackExplicitness.equals(NOT_EXPLICIT_VALUE, ignoreCase = true).not()
            val isAlbumExplicit =
                trackExplicitness.equals(NOT_EXPLICIT_VALUE, ignoreCase = true).not()

            return Track(
                id = trackId.toString(),
                name = if (isExplicit) trackCensoredName else trackName,
                artistName = artistName,
                artworkUrl = artworkUrl100,
                isExplicit = isExplicit,
                albumName = if (isAlbumExplicit) collectionCensoredName else collectionName,
                artistId = artistId.toString(),
                albumId = collectionId.toString(),
            )
        }
    }

    fun toDomain(): SearchResult = SearchResult(
        trackList = results.map { it.toDomain() }
    )
}
