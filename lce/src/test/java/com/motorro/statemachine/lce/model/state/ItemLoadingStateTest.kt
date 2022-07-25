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

package com.motorro.statemachine.lce.model.state

import com.motorro.statemachine.lce.data.ItemId
import com.motorro.statemachine.lce.data.LceGesture
import com.motorro.statemachine.lce.data.LceUiState
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertIs

internal class ItemLoadingStateTest : BaseStateTest() {
    private lateinit var state: LoadingState

    private fun createState(itemId: ItemId) {
        state = LoadingState(itemId, Dispatchers.Main)
    }

    @Before
    @OptIn(ExperimentalCoroutinesApi::class)
    fun before() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    @OptIn(ExperimentalCoroutinesApi::class)
    fun after() {
        state.clear()
        Dispatchers.resetMain()
    }

    @Test
    fun displaysLoadingOnStart() = runTest {
        createState(ItemId.LOADS_CONTENT)

        state.start(stateMachine)

        verify {
            stateMachine.setUiState(LceUiState.Loading)
        }
    }

    @Test
    fun transfersToContentWhenLoaded() = runTest {
        createState(ItemId.LOADS_CONTENT)

        state.start(stateMachine)
        advanceUntilIdle()

        verify { stateMachine.setMachineState(withArg { assertIs<ContentState>(it) }) }
    }

    @Test
    fun transfersToErrorWhenNotLoaded() = runTest {
        createState(ItemId.FAILS_WITH_ERROR)

        state.start(stateMachine)
        advanceUntilIdle()

        verify { stateMachine.setMachineState(withArg { assertIs<ErrorState>(it) }) }
    }

    @Test
    fun returnsToListOnBack() = runTest {
        createState(ItemId.LOADS_CONTENT)

        state.start(stateMachine)
        state.process(LceGesture.Back)

        verify {
            stateMachine.setMachineState(withArg { assertIs<ItemListState>(it) })
        }
    }
}