/*
 * Copyright 2022 Nikolai Kotchetkov.
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

package com.motorro.commonstatemachine

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class CommonMachineStateTest {
    private class TestState: CommonMachineState<Int, Int>() {
        var processed = false
        override fun doProcess(gesture: Int) {
            processed = true
        }
        fun checkUiStateUpdate(uiState: Int) = setUiState(uiState)
        fun checkMachineStateUpdate(machineState: TestState) = setMachineState(machineState)
    }

    @Test
    fun stateWillUpdateUiStateIfStarted() {
        val machine = MachineMock<Int, Int>()
        val state = TestState()
        state.start(machine)
        state.checkUiStateUpdate(1)
        assertTrue { machine.uiStates.contains(1) }
    }

    @Test
    fun stateWillFailToUpdateUiStateIfNotStarted() {
        val state = TestState()
        assertFailsWith<IllegalStateException> {
            state.checkUiStateUpdate(1)
        }
    }

    @Test
    fun stateWillProcessGestureIfStarted() {
        val machine = MachineMock<Int, Int>()
        val state = TestState()
        state.start(machine)
        state.process(2)
        assertTrue { state.processed }
    }

    @Test
    fun stateWillFailToProcessGestureIfNotStarted() {
        val state = TestState()
        assertFailsWith<IllegalStateException> {
            state.process(2)
        }
    }

    @Test
    fun stateWillUpdateMachineStateIfStarted() {
        val machine = MachineMock<Int, Int>()
        val state1 = TestState()
        val state2 = TestState()
        state1.start(machine)
        state1.checkMachineStateUpdate(state2)
        assertTrue { machine.machineStates.contains(state2) }
    }

    @Test
    fun stateWillFailToUpdateMachineStateIfNotStarted() {
        val state1 = TestState()
        val state2 = TestState()
        assertFailsWith<IllegalStateException> {
            state1.checkMachineStateUpdate(state2)
        }
    }
}