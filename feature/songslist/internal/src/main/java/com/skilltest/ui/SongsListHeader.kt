package com.skilltest.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.lerp
import com.skilltest.design.component.ActionButton
import com.skilltest.design.component.SearchField
import com.skilltest.design.theme.Dimens
import com.skilltest.design.util.ComponentPreview
import com.skilltest.design.util.PreviewBox
import com.skilltest.ui.viewmodel.SongsListIntent
import com.skilltest.ui.viewmodel.SongsListState
import com.skilltest.design.R as designR

private const val ICON_TRAVEL_MS = 300
private const val FIELD_FADE_MS = 100
private const val SEARCH_ICON_KEY = "search-icon"

@Composable
fun ColumnScope.SongsListHeader(
    state: SongsListState,
    onIntent: (SongsListIntent) -> Unit,
    modifier: Modifier = Modifier,
    // 0 shows the full display-size title, 1 shrinks it to body size.
    titleCollapseFraction: Float = 0f,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    SharedTransitionLayout(modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimens.padding16)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimens.padding20),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.songs_list_title),
                    style = lerp(
                        MaterialTheme.typography.displaySmall,
                        MaterialTheme.typography.bodyLarge,
                        titleCollapseFraction,
                    )
                )

                AnimatedVisibility(
                    visible = state.isSearching.not(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    ActionButton(
                        content = {
                            Icon(
                                modifier = Modifier.sharedElement(
                                    rememberSharedContentState(SEARCH_ICON_KEY),
                                    animatedVisibilityScope = this@AnimatedVisibility,
                                    boundsTransform = SearchBoundsTransform
                                ),
                                painter = painterResource(designR.drawable.ic_search),
                                contentDescription = stringResource(R.string.acc_search_a_song)
                            )
                        },
                        onClick = {
                            onIntent(SongsListIntent.SearchClick)
                        }
                    )
                }
            }
            AnimatedVisibility(
                visible = state.isSearching,
                enter = expandVertically(
                    animationSpec = tween(ICON_TRAVEL_MS / 2),
                    expandFrom = Alignment.Top
                ),
                exit = shrinkVertically(
                    animationSpec = tween(FIELD_FADE_MS / 2, delayMillis = ICON_TRAVEL_MS / 2),
                    shrinkTowards = Alignment.Top
                ),
            ) {
                val fieldAlpha by transition.animateFloat(
                    transitionSpec = {
                        if (targetState == EnterExitState.Visible) {
                            tween(FIELD_FADE_MS, delayMillis = ICON_TRAVEL_MS)
                        } else {
                            tween(FIELD_FADE_MS)
                        }
                    },
                    label = "searchFieldAlpha",
                ) { state -> if (state == EnterExitState.Visible) 1f else 0f }

                val fieldReady = transition.currentState == EnterExitState.Visible &&
                        transition.targetState == EnterExitState.Visible && !transition.isRunning

                LaunchedEffect(fieldReady) {
                    if (fieldReady) {
                        focusRequester.requestFocus()
                        keyboardController?.show()
                    }
                }


                SearchField(
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .fillMaxWidth(),
                    leadingIconModifier = Modifier.sharedElement(
                        rememberSharedContentState(SEARCH_ICON_KEY),
                        animatedVisibilityScope = this@AnimatedVisibility,
                        boundsTransform = SearchBoundsTransform
                    ),
                    alpha = fieldAlpha,
                    searchTerm = state.searchTerm,
                    onSearchCleared = {
                        onIntent(SongsListIntent.ClearSearch)
                    },
                    onSearchTermChanged = { term ->
                        onIntent(SongsListIntent.StartPerformSearch(term))
                    },
                    onStartedTyping = {
                        onIntent(SongsListIntent.TypingSearchTerm(it))
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
private val SearchBoundsTransform = BoundsTransform { initial, target ->
    val corner = if (target.top > initial.top) {
        initial.translate(0f, target.top - initial.top)
    } else {
        initial.translate(target.left - initial.left, 0f)
    }
    keyframes {
        durationMillis = ICON_TRAVEL_MS
        initial at 0 using FastOutSlowInEasing
        corner at ICON_TRAVEL_MS / 2 using FastOutSlowInEasing
        target at ICON_TRAVEL_MS
    }
}

@ComponentPreview
@Composable
private fun SongsListHeaderPreview() {
    PreviewBox {
        Column {
            SongsListHeader(state = SongsListState(), onIntent = {})
        }
    }
}
