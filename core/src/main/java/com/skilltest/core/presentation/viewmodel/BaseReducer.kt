package com.skilltest.core.presentation.viewmodel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseReducer<State: BaseUiState, Intent: BaseIntent, Effect: BaseUiEffect>(
    initialState: State,
) {

    private val mutableState = MutableStateFlow(initialState)
    val state: StateFlow<State> = mutableState.asStateFlow()


    private val mutableEffects = MutableSharedFlow<Effect>(replay = 0)
    val effects: SharedFlow<Effect> = mutableEffects.asSharedFlow()

    suspend fun reduce(intent: Intent): Flow<State> {
        mutableState.tryEmit(handleIntent(intent))

        return state
    }

    protected abstract suspend fun handleIntent(intent: Intent): State

    protected fun sendEffect(effect: Effect) {
        mutableEffects.tryEmit(effect)
    }
}
