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

package com.motorro.commonstatemachine

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 * State machine that emits UI state as Flow
 */
open class FlowStateMachine<G: Any, U: Any>(init: () -> CommonMachineState<G, U>) : CommonStateMachine.Base<G, U>(init) {
    /**
     * State mediator
     */
    private val mediator = MutableSharedFlow<U>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    init {
        start()
    }

    /**
     * UI state
     */
    val uiState: SharedFlow<U> = mediator

    /**
     * Updates UI state
     * @param uiState UI state
     */
    final override fun setUiState(uiState: U) {
        mediator.tryEmit(uiState)
    }
}