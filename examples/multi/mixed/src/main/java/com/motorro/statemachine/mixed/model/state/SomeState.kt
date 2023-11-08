/*
 * Copyright 2023 Nikolai Kotchetkov.
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

package com.motorro.statemachine.mixed.model.state

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.statemachine.mixed.model.data.SomeGesture
import com.motorro.statemachine.mixed.model.data.SomeUiState

/**
 * Some machine state with internal data
 */
class SomeState : CommonMachineState<SomeGesture, SomeUiState>() {

    private var isOn = false

    override fun doStart() {
        render()
    }

    override fun doProcess(gesture: SomeGesture) {
        isOn = isOn.not()
        render()
    }

    private fun render() {
        setUiState(if (isOn) SomeUiState.On else SomeUiState.Off)
    }
}