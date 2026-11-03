package com.skilltest.shared

import android.content.ComponentName
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import java.util.concurrent.Future

@Composable
fun rememberManagedMediaController(
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
): State<MediaController?> {
    val appContext = LocalContext.current.applicationContext
    val controller = remember(appContext) { mutableStateOf<MediaController?>(null) }

    DisposableEffect(appContext, lifecycle) {
        var pending: Future<out MediaController>? = null

        fun connect() {
            if (pending != null) return

            val token =
                SessionToken(appContext, ComponentName(appContext, PlaybackService::class.java))
            val future = MediaController.Builder(appContext, token).buildAsync()
            pending = future
            future.addListener({
                if (pending === future) {
                    controller.value = runCatching { future.get() }.getOrNull()
                }
            }, ContextCompat.getMainExecutor(appContext))
        }

        fun disconnect() {
            pending?.let(MediaController::releaseFuture)
            pending = null
            controller.value = null
        }

        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> connect()
                Lifecycle.Event.ON_STOP -> disconnect()
                else -> Unit
            }
        }
        lifecycle.addObserver(observer)
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) connect()

        onDispose {
            lifecycle.removeObserver(observer)
            disconnect()
        }
    }

    return controller
}
