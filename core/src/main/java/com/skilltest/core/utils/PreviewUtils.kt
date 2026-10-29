package com.skilltest.core.utils

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark

@PreviewLightDark
//@PreviewFontScale
annotation class ScreenPreview

@Preview(
    showBackground = true,
    backgroundColor = 0xfff,
    name = "Light mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    showBackground = true,
    backgroundColor = 0x000,
    name = "Dark mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@PreviewFontScale
annotation class ComponentPreview
