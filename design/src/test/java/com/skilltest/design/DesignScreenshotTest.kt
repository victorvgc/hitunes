package com.skilltest.design

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.takahirom.roborazzi.captureRoboImage
import com.skilltest.design.component.SearchField
import com.skilltest.design.theme.Dimens
import com.skilltest.design.theme.HiTunesTheme
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35], qualifiers = "w411dp-h891dp")
class DesignScreenshotTest {
    @Test
    fun searchFieldWithQuery() {
        captureRoboImage {
            SnapshotSurface(darkTheme = false) {
                SearchField(
                    searchTerm = "Thriller",
                    onSearchTermChanged = {},
                    onStartedTyping = {},
                    onSearchCleared = {},
                )
            }
        }
    }

    @Test
    fun searchFieldWithQueryInDarkTheme() {
        captureRoboImage {
            SnapshotSurface(darkTheme = true) {
                SearchField(
                    searchTerm = "Thriller",
                    onSearchTermChanged = {},
                    onStartedTyping = {},
                    onSearchCleared = {},
                )
            }
        }
    }

    @Composable
    private fun SnapshotSurface(darkTheme: Boolean, content: @Composable () -> Unit) {
        HiTunesTheme(darkTheme = darkTheme) {
            Column(
                modifier = Modifier
                    .width(Dimens.size360)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(Dimens.padding16),
            ) {
                content()
            }
        }
    }
}
