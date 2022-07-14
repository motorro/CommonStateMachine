@file:OptIn(ExperimentalCoroutinesApi::class)

package com.motorro.commonstatemachine

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FlowStateMachineTest {
    private val state = StateMock<Int, Int>()
    private val stateMachine = FlowStateMachine { state }

    @Test
    fun updatesUiStateCollector() = runTest {

        val values = mutableListOf<Int>()
        val collectJob = launch(UnconfinedTestDispatcher()) {
            stateMachine.uiState.toList(values)
        }

        state.doSetUiState(1)
        assertEquals(1, values[0])

        state.doSetUiState(2)
        state.doSetUiState(3)
        assertEquals(3, values[2])

        assertEquals(3, values.size)

        collectJob.cancel()
    }

    @Test
    fun updatesLateUiStateCollector() = runTest {
        state.doSetUiState(1)
        state.doSetUiState(2)
        state.doSetUiState(3)

        val values = mutableListOf<Int>()
        val collectJob = launch(UnconfinedTestDispatcher()) {
            stateMachine.uiState.toList(values)
        }

        assertEquals(3, values[0])
        assertEquals(1, values.size)

        collectJob.cancel()
    }
}