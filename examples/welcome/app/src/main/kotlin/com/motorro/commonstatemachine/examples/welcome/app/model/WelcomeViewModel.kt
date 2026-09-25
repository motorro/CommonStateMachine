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

package com.motorro.commonstatemachine.examples.welcome.app.model

import androidx.lifecycle.ViewModel
import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.coroutines.FlowStateMachine
import com.motorro.commonstatemachine.examples.welcome.app.data.WelcomeGesture
import com.motorro.commonstatemachine.examples.welcome.app.data.WelcomeUiState
import com.motorro.commonstatemachine.examples.welcome.app.model.state.WelcomeStateFactory
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class WelcomeViewModel(private val factory: WelcomeStateFactory) : ViewModel() {

    private val stateMachine = FlowStateMachine(WelcomeUiState.Loading, ::initializeStateMachine)

    private fun initializeStateMachine(): CommonMachineState<WelcomeGesture, WelcomeUiState> {
        Napier.d("Initializing state machine...")
        return factory.preload()
    }

    val state: Flow<WelcomeUiState> = stateMachine.uiState

    fun process(gesture: WelcomeGesture) {
        Napier.d("Gesture: $gesture")
        stateMachine.process(gesture)
    }

    override fun onCleared() {
        stateMachine.clear()
    }
}
