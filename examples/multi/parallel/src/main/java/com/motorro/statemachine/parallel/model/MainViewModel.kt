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

package com.motorro.statemachine.parallel.model

import androidx.lifecycle.ViewModel
import com.motorro.commonstatemachine.coroutines.FlowStateMachine
import com.motorro.statemachine.parallel.model.data.ParallelGesture
import com.motorro.statemachine.parallel.model.data.ParallelUiState
import com.motorro.statemachine.parallel.model.state.ParallelState
import com.motorro.statemachine.timer.data.TimerUiState
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration

class MainViewModel : ViewModel() {
    private val machine = FlowStateMachine(ParallelUiState(TimerUiState.Stopped(Duration.ZERO), TimerUiState.Stopped(Duration.ZERO))) {
        ParallelState()
    }

    val uiState: StateFlow<ParallelUiState> get() = machine.uiState

    fun update(gesture: ParallelGesture) = machine.process(gesture)

    override fun onCleared() {
        machine.clear()
    }
}