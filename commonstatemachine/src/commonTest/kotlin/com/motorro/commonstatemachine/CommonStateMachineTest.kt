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
import kotlin.test.assertTrue

class CommonStateMachineTest {
    private class TestMachine(state: CommonMachineState<Int, Int>) : CommonStateMachine.Base<Int, Int>( { state }) {

        init {
            start()
        }

        override fun setUiState(uiState: Int) = Unit
    }

    @Test
    fun stateMachineStartsInitialState() {
        val state = StateMock<Int, Int>()
        TestMachine(state)
        assertTrue { state.started }
    }

    @Test
    fun cleansUpStateOnStateChange() {
        val state1 = StateMock<Int, Int>()
        val state2 = StateMock<Int, Int>()
        val machine = TestMachine(state1)
        machine.setMachineState(state2)
        assertTrue { state1.cleared }
    }

    @Test
    fun startsNewState() {
        val state1 = StateMock<Int, Int>()
        val state2 = StateMock<Int, Int>()
        val machine = TestMachine(state1)
        machine.setMachineState(state2)
        assertTrue { state2.started }
    }

    @Test
    fun delegatesGestureToCurrentState() {
        val state = StateMock<Int, Int>()
        val machine = TestMachine(state)
        machine.process(2)
        assertTrue { state.processed.contains(2) }
    }
}