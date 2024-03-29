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
import com.motorro.commonstatemachine.lifecycle.MachineLifecycle

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
     * [MachineLifecycle] passed to the factory determines the activity of
     * the machine within the machine group. For example, for a paging screen
     * you may want to stop some pending operations when active machine is not
     * active anymore
     */
    val init: (MachineLifecycle) -> CommonMachineState<G, U>
}


