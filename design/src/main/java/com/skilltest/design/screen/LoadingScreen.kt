package com.skilltest.design.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.skilltest.design.util.ScreenPreview
import com.skilltest.design.util.PreviewBox

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    progress: Float? = null,
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (progress != null) {
            LoadingIndicator(
                progress = {
                    progress
                }
            )
        } else {
            LoadingIndicator()
        }
    }
}

@ScreenPreview
@Composable
private fun LoadingScreenPreview() {
    PreviewBox {
        LoadingScreen()
    }
}
