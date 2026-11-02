package com.skilltest.core.presentation

import com.skilltest.core.presentation.util.DispatcherProvider
import com.skilltest.core.presentation.viewmodel.BaseIntent
import com.skilltest.core.presentation.viewmodel.BaseReducer
import com.skilltest.core.presentation.viewmodel.BaseUiEffect
import com.skilltest.core.presentation.viewmodel.BaseUiState
import com.skilltest.core.presentation.viewmodel.BaseViewModel
import com.skilltest.core.presentation.viewmodel.PartialStateChange
import com.skilltest.core.presentation.viewmodel.Reduction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MviFlowTest {
    @Test
    fun intentUpdatesStateEmitsEffectAndProcessesDerivedIntent() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        try {
            val viewModel = TestViewModel(TestReducer(), TestDispatchers(dispatcher))
            val effects = mutableListOf<TestEffect>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.effects.take(1).toList(effects)
            }

            viewModel.onIntent(TestIntent.Start)
            advanceUntilIdle()

            assertEquals(2, viewModel.state.value.count)
            assertEquals(listOf(TestEffect.Done), effects)
            assertEquals("TestState", viewModel.state.value.name)
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun reductionNoneKeepsPreviousState() {
        val previous = TestState(count = 4)
        val unchanged = Reduction.none<TestState, TestIntent, TestEffect>()
            .partialStateChange.reduce(previous)

        assertTrue(previous === unchanged)
    }

    @Test
    fun initialIntentRunsOnceForTheViewModelLifetime() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        try {
            val viewModel = TestViewModel(
                TestReducer(),
                TestDispatchers(dispatcher),
                initialIntent = TestIntent.Start,
            )

            advanceUntilIdle()
            assertEquals(2, viewModel.state.value.count)

            // A screen can leave and re-enter composition while retaining this ViewModel.
            advanceUntilIdle()
            assertEquals(2, viewModel.state.value.count)
        } finally {
            Dispatchers.resetMain()
        }
    }

    private data class TestState(val count: Int = 0) : BaseUiState("TestState")
    private sealed class TestIntent(name: String) : BaseIntent(name) {
        data object Start : TestIntent("Start")
        data object FollowUp : TestIntent("FollowUp")
    }
    private sealed class TestEffect(name: String) : BaseUiEffect(name) {
        data object Done : TestEffect("Done")
    }

    private class TestReducer : BaseReducer<TestState, TestIntent, TestEffect> {
        override suspend fun reduce(
            previousState: TestState,
            intent: TestIntent,
        ): Reduction<TestState, TestIntent, TestEffect> = when (intent) {
            TestIntent.Start -> Reduction<TestState, TestIntent, TestEffect>(
                partialStateChange = PartialStateChange<TestState> { it.copy(count = it.count + 1) },
                effects = listOf(TestEffect.Done),
                derivedIntents = listOf(TestIntent.FollowUp),
            )
            TestIntent.FollowUp -> Reduction.state<TestState, TestIntent, TestEffect>(
                PartialStateChange { it.copy(count = it.count + 1) }
            )
        }
    }

    private class TestViewModel(
        reducer: TestReducer,
        dispatchers: DispatcherProvider,
        initialIntent: TestIntent? = null,
    ) : BaseViewModel<TestState, TestIntent, TestEffect>(
        initialState = TestState(),
        reducer = reducer,
        dispatcherProvider = dispatchers,
        initialIntent = initialIntent,
    )

    private class TestDispatchers(private val dispatcher: CoroutineDispatcher) : DispatcherProvider {
        override val main = dispatcher
        override val io = dispatcher
        override val default = dispatcher
    }
}
