package com.skilltest.hitunes.wear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.MaterialTheme
import com.skilltest.hitunes.wear.ui.WearPlayerContent

class WearPlayerActivity : ComponentActivity() {
    private val viewModel: WearPlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val nowPlaying by viewModel.nowPlaying.collectAsState()
            MaterialTheme {
                AppScaffold {
                    WearPlayerContent(
                        nowPlaying = nowPlaying,
                        onCommand = viewModel::send,
                    )
                }
            }
        }
    }
}
