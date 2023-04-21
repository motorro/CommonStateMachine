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

@file:OptIn(ExperimentalCoroutinesApi::class)

package com.motorro.commonstatemachine.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FlowStateMachineTest {
    private val state = StateMock<Int, Int>()
    private val stateMachine = FlowStateMachine(0) { state }

    @Test
    fun updatesUiStateCollector() = runTest(UnconfinedTestDispatcher()) {

        val values = mutableListOf<Int>()
        val collectJob = launch {
            stateMachine.uiState.toList(values)
        }

        assertEquals(0, values[0])

        state.doSetUiState(1)
        assertEquals(1, values[1])

        state.doSetUiState(2)
        state.doSetUiState(3)
        assertEquals(3, values[3])

        assertEquals(4, values.size)

        collectJob.cancel()
    }

    @Test
    fun updatesLateUiStateCollector() = runTest(UnconfinedTestDispatcher()) {
        state.doSetUiState(1)
        state.doSetUiState(2)
        state.doSetUiState(3)

        val values = mutableListOf<Int>()
        val collectJob = launch {
            stateMachine.uiState.toList(values)
        }

        assertEquals(3, values[0])
        assertEquals(1, values.size)

        collectJob.cancel()
    }

    @Test
    fun notifiesOnActiveAndInactive() = runTest(UnconfinedTestDispatcher()) {
        val activeScope = CoroutineScope(SupervisorJob() + UnconfinedTestDispatcher())
        stateMachine.mapUiSubscriptions(activeScope, onActive = { 1 }, onInactive = { 2 })

        assertEquals(listOf(2), state.processed)

        val collectJob = launch {
            stateMachine.uiState.collect()
        }
        collectJob.cancel()
        activeScope.cancel()

        assertEquals(listOf(2, 1, 2), state.processed)
    }
}