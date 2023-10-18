package com.motorro.statemachine.multi.state

import com.motorro.commonstatemachine.lifecycle.LifecycleState
import com.motorro.statemachine.multi.data.TimerGesture
import com.motorro.statemachine.multi.data.TimerUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.seconds

@ExperimentalCoroutinesApi
class RunningTest {
    private lateinit var machine: MachineMock<TimerGesture, TimerUiState>
    private lateinit var lifecycle: MockLifecycleState
    private lateinit var state: TimerState

    @BeforeTest
    fun init() {
        Dispatchers.setMain(StandardTestDispatcher())

        machine = MachineMock(TimerUiState.Stopped(ZERO))
        lifecycle = MockLifecycleState(LifecycleState.State.ACTIVE)
        state = Running(lifecycle, ZERO)
    }

    @AfterTest
    fun after() {
        Dispatchers.resetMain()
    }

    @Test
    fun runsAndCountsWhenActive() = runTest {
        state.start(machine)
        advanceTimeBy(Running.DELAY)
        advanceTimeBy(Running.DELAY)
        advanceTimeBy(Running.DELAY)
        state.clear()

        assertEquals(
            listOf(
                TimerUiState.Stopped(ZERO),
                TimerUiState.Running(ZERO),
                TimerUiState.Running(1.seconds),
                TimerUiState.Running(2.seconds)
            ),
            machine.uiStates
        )
    }

    @Test
    fun stopsCountingWhenInactive() = runTest {
        state.start(machine)
        advanceTimeBy(Running.DELAY)
        lifecycle.deactivate()
        advanceTimeBy(Running.DELAY)
        advanceTimeBy(Running.DELAY)
        advanceTimeBy(Running.DELAY)
        state.clear()

        assertEquals(
            listOf(
                TimerUiState.Stopped(ZERO),
                TimerUiState.Running(ZERO),
                TimerUiState.Running(1.seconds)
            ),
            machine.uiStates
        )
    }

    @Test
    fun countsAgainWhenReactivated() = runTest {
        state.start(machine)
        advanceTimeBy(Running.DELAY)
        lifecycle.deactivate()
        advanceTimeBy(Running.DELAY)
        lifecycle.activate()
        advanceTimeBy(Running.DELAY)
        advanceTimeBy(Running.DELAY)
        state.clear()

        assertEquals(
            listOf(
                TimerUiState.Stopped(ZERO),
                TimerUiState.Running(ZERO),
                TimerUiState.Running(1.seconds),
                TimerUiState.Running(2.seconds)
            ),
            machine.uiStates
        )
    }

    @Test
    fun movesToStoppedOnToggle() = runTest{
        state.start(machine)
        advanceTimeBy(Running.DELAY)
        advanceTimeBy(Running.DELAY)
        state.process(TimerGesture.Toggle)
        state.clear()

        assertTrue { machine.machineStates.first() is Stopped }
    }
}