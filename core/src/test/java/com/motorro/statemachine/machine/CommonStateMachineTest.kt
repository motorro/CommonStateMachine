package com.motorro.statemachine.machine

import io.mockk.spyk
import io.mockk.verify
import org.junit.Test

class CommonStateMachineTest {
    private class TestMachine(state: CommonMachineState<Int, Int>) : CommonStateMachine.Base<Int, Int>( { state }) {

        init {
            start()
        }

        override fun setUiState(uiState: Int) = Unit
    }

    @Test
    fun stateMachineStartsInitialState() {
        val state = spyk(CommonMachineState<Int, Int>())
        val machine = TestMachine(state)
        verify { state.start(machine) }
    }

    @Test
    fun cleansUpStateOnStateChange() {
        val state1 = spyk(CommonMachineState<Int, Int>())
        val state2 = spyk(CommonMachineState<Int, Int>())
        val machine = TestMachine(state1)
        machine.setMachineState(state2)
        verify { state1.clear() }
    }

    @Test
    fun startsNewState() {
        val state1 = spyk(CommonMachineState<Int, Int>())
        val state2 = spyk(CommonMachineState<Int, Int>())
        val machine = TestMachine(state1)
        machine.setMachineState(state2)
        verify { state2.start(machine) }
    }

    @Test
    fun delegatesGestureToCurrentState() {
        val state = spyk(CommonMachineState<Int, Int>())
        val machine = TestMachine(state)
        machine.process(2)
        verify { state.process(2) }
    }
}