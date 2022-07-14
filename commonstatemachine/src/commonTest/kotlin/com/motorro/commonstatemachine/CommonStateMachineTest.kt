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