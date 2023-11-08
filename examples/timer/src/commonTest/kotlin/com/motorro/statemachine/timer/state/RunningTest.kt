/*
 * Copyright 2023 Nikolai Kotchetkov.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.motorro.statemachine.timer.state

import com.motorro.commonstatemachine.lifecycle.ActivatedMachineLifecycle
import com.motorro.commonstatemachine.lifecycle.MachineLifecycle
import com.motorro.statemachine.timer.data.TimerGesture
import com.motorro.statemachine.timer.data.TimerUiState
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
    private lateinit var lifecycle: ActivatedMachineLifecycle
    private lateinit var state: TimerState

    @BeforeTest
    fun init() {
        Dispatchers.setMain(StandardTestDispatcher())

        machine = MachineMock(TimerUiState.Stopped(ZERO))
        lifecycle = ActivatedMachineLifecycle(MachineLifecycle.State.ACTIVE)
        state = Running("", lifecycle, ZERO)
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