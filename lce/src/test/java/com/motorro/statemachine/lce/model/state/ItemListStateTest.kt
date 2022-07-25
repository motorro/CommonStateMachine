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

package com.motorro.statemachine.lce.model.state

import com.motorro.statemachine.lce.data.ItemId
import com.motorro.statemachine.lce.data.LceGesture
import com.motorro.statemachine.lce.data.LceUiState
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertIs

class ItemListStateTest : BaseStateTest() {
    private val state = ItemListState()

    @Test
    fun displaysItemListOnStart() {
        state.start(stateMachine)

        verify {
            stateMachine.setUiState(withArg { assertIs<LceUiState.ItemList>(it) })
        }
    }

    @Test
    fun transfersToLoadingWhenClicked() {
        state.start(stateMachine)
        state.process(LceGesture.ItemClicked(ItemId.LOADS_CONTENT))

        verify {
            stateMachine.setMachineState(withArg { assertIs<LoadingState>(it) })
        }
    }

    @Test
    fun exitsFlowOnBack() {
        state.start(stateMachine)
        state.process(LceGesture.Back)

        verify {
            stateMachine.setMachineState(withArg { assertIs<TerminatedState>(it) })
        }
    }
}