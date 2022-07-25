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