/*
 * Copyright 2026 Nikolai Kotchetkov.
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

package com.motorro.commonstatemachine.flow.data

import com.motorro.commonstatemachine.CommonMachineState

/**
 * Common flow data API. Used as a common interface between master and child flow using proxy.
 * The proxy provides [CommonFlowHost] to the child flow and the child calls its methods when finished
 * or otherwise needed
 * @param G - gesture type
 * @param U - UI state type
 * @param I - input type
 * @param R - result type
 * @see com.motorro.commonstatemachine.ProxyMachineState
 * @see CommonFlowHost
 */
interface CommonFlowDataApi<G: Any, U: Any, I, R> {
    /**
     * Creates flow
     */
    fun init(flowHost: CommonFlowHost<R>, input: I): CommonMachineState<G, U>

    /**
     * Returns default UI state
     */
    fun getDefaultUiState(): U

    /**
     * Returns back gesture mapping
     */
    fun getBackGesture(): G? = null
}