package com.skilltest.domain.model

data class Album(
    val id: String,
    val name: String,
    val coverUrl: String,
    val isExplicit: Boolean,
    val artistName: String,
    val trackList: List<Track>
)
