package com.skilltest.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

// Shrinks the title as the list scrolls up, before the list itself moves, and grows it back
// only once the list has returned to its top.
@Stable
internal class CollapsingTitleState(private val rangePx: Float) {
    private var collapsedPx by mutableFloatStateOf(0f)

    // 0 is the full-size title, 1 is fully collapsed.
    val fraction: Float
        get() = if (rangePx > 0f) collapsedPx / rangePx else 0f

    val nestedScrollConnection = object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            if (available.y >= 0f) return Offset.Zero
            return consumedOffset(-collapseBy(-available.y))
        }

        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource,
        ): Offset {
            if (available.y <= 0f) return Offset.Zero
            return consumedOffset(-collapseBy(-available.y))
        }
    }

    // Offset(0f, -0f) isn't Offset.Zero, which callers compare against.
    private fun consumedOffset(y: Float) = if (y == 0f) Offset.Zero else Offset(0f, y)

    // Returns how much of the delta the title took.
    private fun collapseBy(deltaPx: Float): Float {
        val previous = collapsedPx
        collapsedPx = (previous + deltaPx).coerceIn(0f, rangePx)
        return collapsedPx - previous
    }
}

@Composable
internal fun rememberCollapsingTitleState(range: Dp): CollapsingTitleState {
    val rangePx = with(LocalDensity.current) { range.toPx() }
    return remember(rangePx) { CollapsingTitleState(rangePx) }
}
