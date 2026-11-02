package com.skilltest.design.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.skilltest.design.util.ComponentPreview
import com.skilltest.design.R
import com.skilltest.design.theme.Dimens
import com.skilltest.design.util.PreviewBox
import kotlinx.coroutines.delay
import timber.log.Timber
import kotlin.time.Duration.Companion.seconds

@Composable
fun SearchField(
    searchTerm: String,
    onSearchTermChanged: (String) -> Unit,
    onStartedTyping: (String) -> Unit,
    onSearchCleared: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: (@Composable () -> Unit)? = null,
    leadingIconModifier: Modifier = Modifier,
    alpha: Float = 1f,
) {
    var debounceTerm by remember(searchTerm) { mutableStateOf(searchTerm) }
    Timber.e("testing: $searchTerm -> $debounceTerm")

    LaunchedEffect(debounceTerm) {
        delay(1.seconds)

        onSearchTermChanged(debounceTerm)
    }

    TextField(
        modifier = modifier,
        value = debounceTerm,
        onValueChange = { input ->
            onStartedTyping(input)
            debounceTerm = input
        },
        maxLines = 1,
        singleLine = true,
        shape = MaterialTheme.shapes.medium,
        colors = rememberFadingFieldColors(alpha),
        placeholder = placeholder,
        leadingIcon = {
            Icon(
                modifier = leadingIconModifier,
                painter = painterResource(R.drawable.ic_search),
                contentDescription = null
            )
        },
        trailingIcon = if (debounceTerm.isEmpty()) null else {
            {
                ActionButton(
                    content = {
                        Icon(
                            painter = painterResource(R.drawable.ic_close),
                            contentDescription = null,
                            modifier = Modifier
                                .size(Dimens.size24)
                                .padding(Dimens.padding4),
                        )
                    },
                    onClick = onSearchCleared
                )
            }
        }
    )
}

// Fades everything except the leading icon, which must stay opaque for the shared-element handoff.
@Composable
private fun rememberFadingFieldColors(alpha: Float): TextFieldColors {
    val base = TextFieldDefaults.colors()
    return base.copy(
        focusedTextColor = base.focusedTextColor.copy(alpha = base.focusedTextColor.alpha * alpha),
        unfocusedTextColor = base.unfocusedTextColor.copy(alpha = base.unfocusedTextColor.alpha * alpha),
        focusedContainerColor = base.focusedContainerColor.copy(alpha = base.focusedContainerColor.alpha * alpha),
        unfocusedContainerColor = base.unfocusedContainerColor.copy(alpha = base.unfocusedContainerColor.alpha * alpha),
        focusedPlaceholderColor = base.focusedPlaceholderColor.copy(alpha = base.focusedPlaceholderColor.alpha * alpha),
        unfocusedPlaceholderColor = base.unfocusedPlaceholderColor.copy(alpha = base.unfocusedPlaceholderColor.alpha * alpha),
        focusedTrailingIconColor = base.focusedTrailingIconColor.copy(alpha = base.focusedTrailingIconColor.alpha * alpha),
        unfocusedTrailingIconColor = base.unfocusedTrailingIconColor.copy(alpha = base.unfocusedTrailingIconColor.alpha * alpha),
        cursorColor = base.cursorColor.copy(alpha = base.cursorColor.alpha * alpha),
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
    )
}

@ComponentPreview
@Composable
private fun SearchFieldPreview(
    @PreviewParameter(SearchPreviewParams::class)
    search: String,
) {
    PreviewBox {
        SearchField(
            searchTerm = search,
            onSearchCleared = {},
            onSearchTermChanged = {},
            onStartedTyping = {},
        )
    }
}

private class SearchPreviewParams : PreviewParameterProvider<String> {
    override val values: Sequence<String> = sequenceOf(
        "",
        "Some search term"
    )

}
