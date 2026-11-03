package com.skilltest.core.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skilltest.core.presentation.util.DefaultDispatcherProvider
import com.skilltest.core.presentation.util.DispatcherProvider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import timber.log.Timber

abstract class BaseViewModel<State: BaseUiState, Intent: BaseIntent, Effect: BaseUiEffect>(
    initialState: State,
    private val reducer: BaseReducer<State, Intent, Effect>,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    initialIntent: Intent? = null,
) : ViewModel() {

    private val mutableState = MutableStateFlow<State>(initialState)
    val state: StateFlow<State> = mutableState.asStateFlow()


    private val mutableEffects = MutableSharedFlow<Effect>(
        replay = 0,
        extraBufferCapacity = 16,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val effects: SharedFlow<Effect> = mutableEffects.asSharedFlow()

    private val intentQueue = Channel<Intent>(
        capacity = 64,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
    private val subscriptions = mutableMapOf<Any, Job>()

    init {
        viewModelScope.launch(dispatcherProvider.main) {
            while (true) {
                val intent = receiveNextIntent()

                try {
                    processIntent(intent)
                } catch (e: Throwable) {
                    if (e is CancellationException) {
                        throw e
                    }
                }
            }
        }
        initialIntent?.let(::onIntent)
    }

    fun onIntent(intent: Intent) {
        if (intentQueue.trySend(intent).isSuccess.not()) {
            viewModelScope.launch(dispatcherProvider.main) {
                intentQueue.send(intent)
            }
        }
    }

    private suspend fun receiveNextIntent(): Intent {
        intentQueue.tryReceive().getOrNull()?.let { return it }

        return select {
            intentQueue.onReceive { it }
        }
    }

    private suspend fun processIntent(intent: Intent) {
        val previousState = mutableState.value

        handleAnalytics(previousState, intent)

        val reduction = reducer.reduce(previousState, intent)
        val nextState = reduction.partialStateChange.reduce(previousState)

        if (previousState != nextState) {
            mutableState.value = nextState
        }

        reduction.effects.forEach { effect ->
            emitEffect(
                state = nextState,
                effect = effect,
                originatingIntent = intent
            )
        }

        reduction.derivedIntents.forEach { derivedIntent ->
            onIntent(derivedIntent)
        }

        reduction.subscriptions.forEach { subscription ->
            subscriptions.remove(subscription.key)?.cancel()
            subscriptions[subscription.key] = viewModelScope.launch(dispatcherProvider.main) {
                subscription.intents.collect(::onIntent)
            }
        }
    }

    private suspend fun emitEffect(state: State, effect: Effect, originatingIntent: Intent) {
        mutableEffects.emit(effect)

        handleAnalytics(state, originatingIntent, effect)
    }

    protected open suspend fun handleAnalytics(
        state: State,
        intent: Intent,
        effect: Effect? = null
    ) {
        var msg = "State: ${state.name} => Intent: ${intent.name}"

        effect?.let {
            msg += " created Effect: ${effect.name}"
        }

        Timber.d(msg)
    }
}
