package com.skilltest.internal.ui.viewmodel

import com.skilltest.core.presentation.viewmodel.BaseUiState
import com.skilltest.core.presentation.viewmodel.PartialStateChange
import com.skilltest.domain.model.Album
import com.skilltest.internal.ui.model.UiAlbum

data class AlbumComponentState(
    val isLoading: Boolean = false,
    val album: UiAlbum = UiAlbum()
) : BaseUiState("AlbumComponentState")

internal sealed interface AlbumStateChange : PartialStateChange<AlbumComponentState> {
    data object LoadingDetails : AlbumStateChange {
        override fun reduce(previous: AlbumComponentState): AlbumComponentState =
            previous.copy(
                isLoading = true
            )
    }

    data class DisplayAlbumDetails(
        val album: UiAlbum
    ) : AlbumStateChange {
        override fun reduce(previous: AlbumComponentState): AlbumComponentState =
            previous.copy(
                isLoading = false,
                album = album
            )
    }
}
