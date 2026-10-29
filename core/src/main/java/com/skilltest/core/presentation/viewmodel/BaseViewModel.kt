package com.skilltest.core.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skilltest.core.utils.DefaultDispatcherProvider
import com.skilltest.core.utils.DispatcherProvider
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<State: BaseUiState, Intent: BaseIntent, Effect: BaseUiEffect>(
    initialState: State,
    private val reducer: BaseReducer<State, Intent, Effect>,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
) : ViewModel() {

    companion object {
        private const val DEFAULT_INTENT_BUFFER_CAPACITY = 64
    }

    val state: StateFlow<State> = reducer.state

    val effects: SharedFlow<Effect> = reducer.effects

    private val intentQueue = MutableSharedFlow<Intent>(
        replay = 0,
        extraBufferCapacity = DEFAULT_INTENT_BUFFER_CAPACITY,
        onBufferOverflow = BufferOverflow.SUSPEND
    )

    init {
        viewModelScope.launch(dispatcherProvider.default) {
            intentQueue.handleIntents()
        }
    }

    fun onIntent(intent: Intent) {
        intentQueue.tryEmit(intent)
    }

    protected abstract fun Flow<State>.handleAnalytics(intent: Intent)

    private suspend fun Flow<Intent>.handleIntents() {
        this.collect { intent ->
            reducer
                .reduce(intent)
                .handleAnalytics(intent)
        }
    }
}
