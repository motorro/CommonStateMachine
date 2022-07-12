package com.motorro.statemachine.machine

import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
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
        val machine: CommonStateMachine<Int, Int> = mockk(relaxed = true)
        val state = TestState()
        state.start(machine)
        state.checkUiStateUpdate(1)
        verify { machine.setUiState(1) }
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
        val machine: CommonStateMachine<Int, Int> = mockk(relaxed = true)
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
        val machine: CommonStateMachine<Int, Int> = mockk(relaxed = true)
        val state1 = TestState()
        val state2 = TestState()
        state1.start(machine)
        state1.checkMachineStateUpdate(state2)
        verify { machine.machineState = state2 }
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