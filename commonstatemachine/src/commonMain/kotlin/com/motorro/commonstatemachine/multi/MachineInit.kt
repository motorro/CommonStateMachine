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

package com.motorro.commonstatemachine.multi

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.ProxyStateMachine
import com.motorro.commonstatemachine.lifecycle.LifecycleState

/**
 * Proxy machine initialization record
 */
interface MachineInit<G: Any, U: Any> {
    /**
     * Machine key to find a machine among the others
     * @see UiStateProvider
     * @see GestureProcessor
     * @see MultiMachineState.mapUiState
     * @see MultiMachineState.mapGesture
     */
    val key: MachineKey<G, U>

    /**
     * Initial UI state for the machine
     */
    val initialUiState: U

    /**
     * Creates initial child state
     * [LifecycleState] passed to the factory determines the activity of
     * the machine within the machine group. For example, for a paging screen
     * you may want to stop some pending operations when active machine is not
     * active anymore
     */
    val init: (LifecycleState) -> CommonMachineState<G, U>
}

/**
 * Creates a proxy machine given [MachineInit] structure
 * @param lifeCycle Machine lifecycle within [ProxyMachineContainer]
 * @param onUiChanged UI-state change handler
 */
internal fun <G: Any, U: Any> MachineInit<G, U>.machine(
    lifeCycle: LifecycleState,
    onUiChanged: (key: MachineKey<*, *>, uiState: Any) -> Unit
): ProxyStateMachine<G, U> = ProxyStateMachine(
    initialUiState,
    { init(lifeCycle) },
    { onUiChanged(key, it) }
)

