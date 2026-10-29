package com.skilltest.domain.model

data class Track(
    val id: String,
    val name: String,
    val artistName: String,
    val artworkUrl: String,
    val isExplicit: Boolean,
)
