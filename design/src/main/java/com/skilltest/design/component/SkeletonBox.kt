package com.skilltest.design.component

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.skilltest.core.util.ComponentPreview
import com.skilltest.design.theme.Dimens
import com.skilltest.design.theme.HiTunesTheme
import kotlinx.collections.immutable.persistentListOf

private const val TRANSITION_LABEL = "Skeleton Loading Transition"
private const val ANIMATION_LABEL = "Skeleton Loading Animation"
private const val ANIMATION_SPEED = 600

@Composable
fun SkeletonBox(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = Dimens.radius8,
    animationSpeed: Int = ANIMATION_SPEED,
) {
    Box(
        modifier = modifier
            .shimmerLoading(
                cornerRadius = cornerRadius,
                animationSpeed = animationSpeed
            )
            .clip(MaterialTheme.shapes.medium)
    )
}

@Composable
private fun Modifier.shimmerLoading(
    cornerRadius: Dp = Dimens.radius8,
    animationSpeed: Int,
): Modifier {
    val transition = rememberInfiniteTransition(label = TRANSITION_LABEL)

    val radius = with(LocalDensity.current) {
        cornerRadius.toPx()
    }

    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 500f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(
                durationMillis = animationSpeed,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = ANIMATION_LABEL
    )

    return drawBehind {
        drawRoundRect(
            cornerRadius = CornerRadius(radius),
            brush = linearGradient(
                colors = persistentListOf(
                    Color.LightGray.copy(alpha = 0.2f),
                    Color.LightGray.copy(alpha = 1.0f),
                    Color.LightGray.copy(alpha = 0.2f),
                ),
                start = Offset(x = translateAnimation, y = translateAnimation),
                end = Offset(x = translateAnimation + 100f, y = translateAnimation + 100f),
            )
        )
    }
}

@ComponentPreview
@Composable
private fun SkeletonBoxPreview() {
    HiTunesTheme {
        SkeletonBox(
            modifier = Modifier.size(Dimens.size52)
        )
    }
}
