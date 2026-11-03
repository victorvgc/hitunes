package com.skilltest.design.util

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Configuration.UI_MODE_TYPE_CAR
import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.skilltest.design.theme.HiTunesTheme

private const val PHONE_PORTRAIT = "spec:width=411dp,height=891dp"
private const val TABLET_PORTRAIT = "spec:width=1280dp,height=800dp,dpi=240,orientation=portrait"
private const val TABLET_LANDSCAPE = "spec:width=1280dp,height=800dp,dpi=240"
private const val AUTO = "spec:width=1024dp,height=768dp"

private const val LIGHT = UI_MODE_NIGHT_NO or UI_MODE_TYPE_NORMAL
private const val DARK = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL

@Preview(
    name = "Phone portrait - Light",
    group = "Phone",
    device = PHONE_PORTRAIT,
    uiMode = LIGHT,
    showBackground = true
)
@Preview(
    name = "Phone portrait - Dark",
    group = "Phone",
    device = PHONE_PORTRAIT,
    uiMode = DARK,
    showBackground = true
)
@Preview(
    name = "Tablet portrait - Light",
    group = "Tablet",
    device = TABLET_PORTRAIT,
    uiMode = LIGHT,
    showBackground = true
)
@Preview(
    name = "Tablet portrait - Dark",
    group = "Tablet",
    device = TABLET_PORTRAIT,
    uiMode = DARK,
    showBackground = true
)
@Preview(
    name = "Tablet landscape - Light",
    group = "Tablet",
    device = TABLET_LANDSCAPE,
    uiMode = LIGHT,
    showBackground = true
)
@Preview(
    name = "Tablet landscape - Dark",
    group = "Tablet",
    device = TABLET_LANDSCAPE,
    uiMode = DARK,
    showBackground = true
)
@Preview(
    name = "Auto - Light",
    group = "Auto",
    device = AUTO,
    uiMode = UI_MODE_NIGHT_NO or UI_MODE_TYPE_CAR,
    showBackground = true
)
@Preview(
    name = "Auto - Dark",
    group = "Auto",
    device = AUTO,
    uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_CAR,
    showBackground = true
)
annotation class ScreenPreview

@Preview(
    name = "Auto - Light",
    device = AUTO,
    uiMode = UI_MODE_NIGHT_NO or UI_MODE_TYPE_CAR,
    showBackground = true
)
@Preview(
    name = "Auto - Dark",
    device = AUTO,
    uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_CAR,
    showBackground = true
)
annotation class AutoPreview

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
