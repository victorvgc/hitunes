package com.skilltest.hitunes.ui.layout

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext

enum class AppLayout { SinglePane, TwoPane }

// Material's breakpoint: below 600dp on the short side the device is a phone.
private const val TABLET_MIN_SMALLEST_WIDTH_DP = 600

fun isPhone(smallestWidthDp: Int): Boolean = smallestWidthDp < TABLET_MIN_SMALLEST_WIDTH_DP

// Tablets in landscape and car screens show the song list next to the playing song.
// Phones always use the single pane with the mini player; they are locked to portrait.
fun appLayoutFor(widthDp: Int, heightDp: Int, smallestWidthDp: Int, isCar: Boolean): AppLayout =
    when {
        isCar -> AppLayout.TwoPane
        !isPhone(smallestWidthDp) && widthDp > heightDp -> AppLayout.TwoPane
        else -> AppLayout.SinglePane
    }

fun Context.isAutomotive(): Boolean =
    packageManager.hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE)

@Composable
fun rememberAppLayout(): AppLayout {
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val isAutomotive = remember(context) { context.isAutomotive() }
    val isCarMode = configuration.uiMode and Configuration.UI_MODE_TYPE_MASK ==
            Configuration.UI_MODE_TYPE_CAR
    return appLayoutFor(
        widthDp = configuration.screenWidthDp,
        heightDp = configuration.screenHeightDp,
        smallestWidthDp = configuration.smallestScreenWidthDp,
        isCar = isAutomotive || isCarMode,
    )
}
