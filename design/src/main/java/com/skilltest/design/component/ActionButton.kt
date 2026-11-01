package com.skilltest.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import com.skilltest.core.util.ComponentPreview
import com.skilltest.design.R
import com.skilltest.design.theme.HiTunesTheme

@Composable
fun ActionButton(
    content: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: ActionButtonColors = ActionButtonDefaults.colors(),
    shape: Shape = ActionButtonDefaults.shape(),
) {
    Box(
        modifier = modifier
            .background(
                color = colors.container,
                shape = shape,
            )
            .clip(shape)
            .clickable {
                onClick()
            }
    ) {
        CompositionLocalProvider(
            LocalContentColor provides colors.content,
        ) {
            content()
        }
    }
}

@ComponentPreview
@Composable
private fun ActionButtonPreview() {
    HiTunesTheme {
        ActionButton(
            content = {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = null
                )
            },
            onClick = {}
        )
    }
}

object ActionButtonDefaults {
    @Composable
    fun colors() = ActionButtonColors(
        content = MaterialTheme.colorScheme.onBackground,
        container = MaterialTheme.colorScheme.background
    )

    @Composable
    fun shape() = MaterialTheme.shapes.extraSmall
}

data class ActionButtonColors(
    val content: Color,
    val container: Color,
)
