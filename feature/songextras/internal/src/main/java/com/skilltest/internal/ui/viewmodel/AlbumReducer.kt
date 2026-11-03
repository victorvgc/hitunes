package com.skilltest.internal.ui.viewmodel

import com.skilltest.core.presentation.viewmodel.BaseReducer
import com.skilltest.core.presentation.viewmodel.Reduction
import com.skilltest.domain.usecase.LoadAlbumDetailsUseCase
import com.skilltest.domain.usecase.LoadAlbumDetailsUseCase.Request
import com.skilltest.internal.ui.model.UiAlbum
import com.skilltest.internal.ui.model.UiAlbum.Companion.toUi
import com.skilltest.internal.ui.viewmodel.AlbumComponentEffect.NavigateToAlbumDetails
import com.skilltest.internal.ui.viewmodel.AlbumComponentIntent.LoadAlbumDetails
import com.skilltest.internal.ui.viewmodel.AlbumComponentIntent.OnClickTrack
import com.skilltest.internal.ui.viewmodel.AlbumComponentIntent.OpenAlbumDetails
import com.skilltest.internal.ui.viewmodel.AlbumStateChange.DisplayAlbumDetails
import com.skilltest.internal.ui.viewmodel.AlbumStateChange.LoadingDetails as LoadingDetailsState

class AlbumReducer(
    private val loadAlbumDetailsUseCase: LoadAlbumDetailsUseCase
) : BaseReducer<AlbumComponentState, AlbumComponentIntent, AlbumComponentEffect> {
    override suspend fun reduce(
        previousState: AlbumComponentState,
        intent: AlbumComponentIntent
    ): Reduction<AlbumComponentState, AlbumComponentIntent, AlbumComponentEffect> = when (intent) {
        is OpenAlbumDetails -> {
            Reduction(
                derivedIntents = listOf(AlbumComponentIntent.LoadingDetails(intent.albumId)),
                effects = listOf(
                    NavigateToAlbumDetails(
                        albumId = intent.albumId,
                        trackId = intent.trackId,
                        trackName = intent.trackName,
                        artistId = intent.artistId,
                        artistName = intent.artistName,
                    )
                )
            )
        }

        is LoadAlbumDetails -> {
            val result = loadAlbumDetailsUseCase(Request(intent.albumId))

            val uiAlbum = result.data?.toUi() ?: UiAlbum()

            Reduction.state(DisplayAlbumDetails(uiAlbum))
        }

        is AlbumComponentIntent.LoadingDetails -> {
            Reduction(
                partialStateChange = LoadingDetailsState,
                derivedIntents = listOf(
                    LoadAlbumDetails(intent.albumId)
                )
            )
        }

        is OnClickTrack -> {
            Reduction.effect(AlbumComponentEffect.NavigateToSongDetails(intent.track.toDomain()))
        }
    }
}
