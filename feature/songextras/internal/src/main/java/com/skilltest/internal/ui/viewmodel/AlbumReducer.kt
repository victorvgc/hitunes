package com.skilltest.internal.ui.viewmodel

import com.skilltest.core.presentation.viewmodel.BaseReducer
import com.skilltest.core.presentation.viewmodel.Reduction

class AlbumReducer: BaseReducer<AlbumComponentState, AlbumComponentIntent, AlbumComponentEffect> {
    override suspend fun reduce(
        previousState: AlbumComponentState,
        intent: AlbumComponentIntent
    ): Reduction<AlbumComponentState, AlbumComponentIntent, AlbumComponentEffect> = when (intent) {
        is AlbumComponentIntent.OpenAlbumDetails -> Reduction.effect(
            AlbumComponentEffect.NavigateToAlbumDetails(
                albumId = intent.albumId,
                trackId = intent.trackId,
                trackName = intent.trackName,
                artistId = intent.artistId,
                artistName = intent.artistName,
            )
        )
    }
}
