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

package com.motorro.commonstatemachine.coroutines

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.CommonStateMachine

class StateMock<G: Any, U: Any> : CommonMachineState<G, U>() {
    var started = false
    val processed = mutableListOf<G>()
    var cleared = false

    override fun doStart() {
        started = true
    }

    override fun doProcess(gesture: G) {
        processed.add(gesture)
    }

    override fun doClear() {
        cleared = true
    }

    fun doSetUiState(state: U) {
        this.setUiState(state)
    }
}

class MachineMock<G: Any, U: Any> : CommonStateMachine<G, U> {
    val machineStates = mutableListOf<CommonMachineState<G, U>>()
    val processed = mutableListOf<G>()
    val uiStates = mutableListOf<U>()
    var cleared = false

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