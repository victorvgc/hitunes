package com.skilltest.design.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkBackground,
    secondary = DarkTextSecondary,
    onSecondary = DarkBackground,
    tertiary = DarkTextMuted,
    onTertiary = DarkTextPrimary,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkBackground,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkInputSurface,
    onSurfaceVariant = DarkTextSecondary,
    surfaceContainerLow = DarkSheetSurface,
    surfaceContainer = DarkSheetSurface,
    surfaceContainerHigh = DarkSheetSurface,
    surfaceContainerHighest = DarkInputSurface,
    outline = DarkTextMuted,
    outlineVariant = DarkInputSurface,
    scrim = DarkBackground,
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightBackground,
    secondary = LightTextSecondary,
    onSecondary = LightBackground,
    tertiary = LightTextMuted,
    onTertiary = LightBackground,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightBackground,
    onSurface = LightTextPrimary,
    surfaceVariant = LightInputSurface,
    onSurfaceVariant = LightTextSecondary,
    surfaceContainerLow = LightSheetSurface,
    surfaceContainer = LightSheetSurface,
    surfaceContainerHigh = LightSheetSurface,
    surfaceContainerHighest = LightInputSurface,
    outline = LightTextMuted,
    outlineVariant = LightInputSurface,
    scrim = LightTextPrimary,
)

private val Shapes = Shapes(
    extraSmall = CircleShape,
)

@Composable
fun HiTunesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
