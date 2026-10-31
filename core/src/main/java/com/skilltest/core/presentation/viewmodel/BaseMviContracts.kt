package com.skilltest.core.presentation.viewmodel

interface BaseReducer<State : BaseUiState, Intent : BaseIntent, Effect : BaseUiEffect> {
    suspend fun reduce(previousState: State, intent: Intent): Reduction<State, Intent, Effect>
}

data class Reduction<State : BaseUiState, Intent : BaseIntent, Effect : BaseUiEffect>(
    val partialStateChange: PartialStateChange<State> = PartialStateChange.none(),
    val effects: List<Effect> = emptyList(),
    val derivedIntents: List<Intent> = emptyList()
) {
    companion object {
        fun <State : BaseUiState, Intent : BaseIntent, Effect : BaseUiEffect> none() =
            Reduction<State, Intent, Effect>()

        fun <State : BaseUiState, Intent : BaseIntent, Effect : BaseUiEffect> effect(
            effect: Effect
        ) = Reduction<State, Intent, Effect>(effects = listOf(effect))

        fun <State : BaseUiState, Intent : BaseIntent, Effect : BaseUiEffect> derivedIntent(
            intent: Intent
        ) = Reduction<State, Intent, Effect>(derivedIntents = listOf(intent))

        fun <State : BaseUiState, Intent : BaseIntent, Effect : BaseUiEffect> state(
            state: PartialStateChange<State>
        ) = Reduction<State, Intent, Effect>(partialStateChange = state)
    }
}

fun interface PartialStateChange<State> {
    fun reduce(previous: State): State

    companion object {
        fun <State> none(): PartialStateChange<State> = PartialStateChange { it }
    }
}
