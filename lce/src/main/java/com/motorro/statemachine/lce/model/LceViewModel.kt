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

package com.motorro.statemachine.lce.model

import androidx.lifecycle.ViewModel
import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.coroutines.FlowStateMachine
import com.motorro.statemachine.lce.data.LceGesture
import com.motorro.statemachine.lce.data.LceUiState
import com.motorro.statemachine.lce.model.state.ItemListState
import kotlinx.coroutines.flow.SharedFlow
import timber.log.Timber

/**
 * Wraps state-machine with view-model
 */
class LceViewModel : ViewModel() {
    /**
     * Creates initial state for state-machine
     */
    private fun initStateMachine(): CommonMachineState<LceGesture, LceUiState> = ItemListState()

    /**
     * State-machine instance
     */
    private val stateMachine = FlowStateMachine(::initStateMachine)

    /**
     * UI State
     */
    val state: SharedFlow<LceUiState> = stateMachine.uiState

    /**
     * Updates state with UI gesture
     * @param gesture UI gesture to proceed
     */
    fun process(gesture: LceGesture) {
        Timber.d("Gesture: %s", gesture)
        stateMachine.process(gesture)
    }

    override fun onCleared() {
        stateMachine.clear()
    }
}