package com.skilltest.data.remote.model

import com.skilltest.domain.model.SearchResult

data class RemoteSearchResult(
    val resultCount: Int? = null,
    val results: List<RemoteEntity?>? = null,
) {
    fun toDomain(): SearchResult = SearchResult(
        trackList = results.orEmpty().mapNotNull { it?.toTrackDomain() }
    )
}
