package com.skilltest.design.util

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun Modifier.sharedImageTransition(
    size: Dp,
    trackId: String,
    sharedTransitionScope: SharedTransitionScope?,
    animatedVisibilityScope: AnimatedVisibilityScope?,
): Modifier {
    if (sharedTransitionScope == null || animatedVisibilityScope == null) {
        return this.size(size)
    }
    return with(sharedTransitionScope) {
        Modifier
            .sharedElement(
                rememberSharedContentState(key = "player-artwork-$trackId"),
                animatedVisibilityScope = animatedVisibilityScope,
            )
            .size(size)
    }
}
