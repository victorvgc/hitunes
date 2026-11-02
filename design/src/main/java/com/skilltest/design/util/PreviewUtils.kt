package com.skilltest.design.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.skilltest.design.theme.HiTunesTheme

@PreviewLightDark
//@PreviewFontScale
annotation class ScreenPreview

@PreviewLightDark
@PreviewFontScale
annotation class ComponentPreview

@Composable
fun PreviewBox(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    HiTunesTheme {
        Box(
            modifier = modifier.background(
                color = MaterialTheme.colorScheme.background
            ),
        ) {
            content()
        }
    }
}
