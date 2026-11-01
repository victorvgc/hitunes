package com.skilltest.internal.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.skilltest.design.theme.Dimens
import com.skilltest.design.util.PreviewBox
import com.skilltest.design.util.ScreenPreview

@Composable
fun AlbumDetails(
    albumId: String,
    trackId: String,
    trackName: String,
    artistId: String,
    artistName: String,
) {
    Column(modifier = Modifier.padding(Dimens.padding24)) {
        Text(text = trackName, style = MaterialTheme.typography.headlineMedium)
        Text(text = artistName, style = MaterialTheme.typography.titleMedium)
        Text(text = albumId, style = MaterialTheme.typography.bodySmall)
    }
}

@ScreenPreview
@Composable
private fun AlbumDetailsPreview() {
    PreviewBox {
        AlbumDetails(
            albumId = "album-2",
            trackId = "track-1",
            trackName = "Thriller",
            artistId = "artist-3",
            artistName = "Michael Jackson",
        )
    }
}
