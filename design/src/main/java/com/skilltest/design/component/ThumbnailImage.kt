package com.skilltest.design.component

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import com.skilltest.core.utils.ComponentPreview
import com.skilltest.design.theme.HiTunesTheme

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ThumbnailImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    placeholder: Painter? = null,
    error: Painter? = null
) {
    val previewColor = MaterialTheme.colorScheme.primary.toArgb()

    val previewHandler = AsyncImagePreviewHandler {
        ColorImage(previewColor)
    }

    CompositionLocalProvider(
        LocalAsyncImagePreviewHandler provides previewHandler
    ) {
        AsyncImage(
            modifier = modifier
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = shape
                )
                .clip(shape),
            model = url,
            contentDescription = contentDescription,
        )
    }
}

@ComponentPreview
@Composable
private fun ThumbnailImagePreview() {
    HiTunesTheme() {
        ThumbnailImage(
            url = "",
            contentDescription = null
        )
    }
}
