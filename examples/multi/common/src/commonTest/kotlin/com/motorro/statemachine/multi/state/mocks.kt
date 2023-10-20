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

package com.motorro.statemachine.multi.state

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.CommonStateMachine

class MachineMock<G: Any, U: Any>(uiState: U) : CommonStateMachine<G, U> {
    val machineStates = mutableListOf<CommonMachineState<G, U>>()
    val processed = mutableListOf<G>()
    val uiStates = mutableListOf(uiState)
    var cleared = false

    override fun isStarted(): Boolean = true

    override fun getUiState(): U = uiStates.last()

    override fun setMachineState(machineState: CommonMachineState<G, U>) {
        machineStates.add(machineState)
    }

    override fun process(gesture: G) {
        processed.add(gesture)
    }

    override fun setUiState(uiState: U) {
        uiStates.add(uiState)
    }

    override fun clear() {
        cleared = true
    }
}
