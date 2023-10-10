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

package com.motorro.commonstatemachine

/**
 * State-machine used in proxies
 * @param uiState Initial UI state
 * @param init Initializes the machine
 * @param onUiStateChange Called when UI state changes
 */
internal class ProxyStateMachine<G : Any, U : Any>(
    private var uiState: U,
    init: () -> CommonMachineState<G, U>,
    onUiStateChange: (U) -> Unit
) : CommonStateMachine.Base<G, U>(init) {
    /**
     * Updates upstream
     * When cleared - set to null
     */
    private var uiStateChangeHandler: ((U) -> Unit)? = onUiStateChange

    /**
     * Current UI state
     * @return current UI state or `null` if not yet available
     */
    override fun getUiState(): U = uiState

    /**
     * Updates UI state
     * @param uiState UI state
     */
    override fun setUiState(uiState: U) {
        this.uiState = uiState
        uiStateChangeHandler?.invoke(uiState)
    }

    /**
     * Runs machine
     */
    fun doStart() = start()

    /**
     * Cleans-up the machine
     */
    fun doClear() {
        uiStateChangeHandler = null
        clear()
    }
}