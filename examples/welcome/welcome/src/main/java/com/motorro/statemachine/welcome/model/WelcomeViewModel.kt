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

package com.motorro.statemachine.welcome.model

import androidx.lifecycle.ViewModel
import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.coroutines.FlowStateMachine
import com.motorro.statemachine.welcome.data.WelcomeGesture
import com.motorro.statemachine.welcome.data.WelcomeUiState
import com.motorro.statemachine.welcome.model.state.WelcomeStateFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class WelcomeViewModel @Inject constructor(private val factory: WelcomeStateFactory) : ViewModel() {
    /**
     * State machine
     */
    private val stateMachine = FlowStateMachine(WelcomeUiState.Loading, ::initializeStateMachine)

    /**
     * Creates initializing state
     */
    private fun initializeStateMachine(): CommonMachineState<WelcomeGesture, WelcomeUiState> {
        Timber.d("Initializing state machine...")
        return factory.preload()
    }

    /**
     * UI State
     */
    val state: Flow<WelcomeUiState> = stateMachine.uiState

    /**
     * Updates state with UI gesture
     * @param gesture UI gesture to proceed
     */
    fun process(gesture: WelcomeGesture) {
        Timber.d("Gesture: %s", gesture)
        stateMachine.process(gesture)
    }

    /**
     * This method will be called when this ViewModel is no longer used and will be destroyed.
     *
     *
     * It is useful when ViewModel observes some data and you need to clear this subscription to
     * prevent a leak of this ViewModel.
     */
    override fun onCleared() {
        stateMachine.clear()
    }
}