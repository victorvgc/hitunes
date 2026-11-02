package com.skilltest.data.remote.model

import com.skilltest.domain.model.Album

data class RemoteAlbum(
    val resultCount: Int? = null,
    val results: List<RemoteEntity>? = null,
) {
    companion object {
        private const val NOT_EXPLICIT_VALUE = "notExplicit"
        private const val WRAPPER_COLLECTION = "collection"
    }

    fun toDomain(): Album {
        val entities = results.orEmpty()
        val collection = entities.firstOrNull {
            it.wrapperType.equals(WRAPPER_COLLECTION, ignoreCase = true)
        }
        val isExplicit = collection?.collectionExplicitness
            ?.equals(NOT_EXPLICIT_VALUE, ignoreCase = true) == false

        return Album(
            id = collection?.collectionId?.toString().orEmpty(),
            name = collection?.let {
                if (isExplicit) it.collectionCensoredName else it.collectionName
            }.orEmpty(),
            isExplicit = isExplicit,
            trackList = entities.mapNotNull { it.toTrackDomain() },
            coverUrl = collection?.artworkUrl100.orEmpty(),
            artistName = collection?.artistName.orEmpty(),
        )
    }
}
