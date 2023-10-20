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
import com.motorro.commonstatemachine.CommonStateMachine
import com.motorro.commonstatemachine.ProxyStateMachine
import com.motorro.commonstatemachine.lifecycle.ActivatedLifecycleState
import com.motorro.commonstatemachine.lifecycle.LifecycleState

/**
 * State-machine wrapper that activates and deactivates
 * @param init Machine init
 * @param onUiChanged UI-state change handler
 */
internal class ActiveStateMachine<G: Any, U: Any>(
    init: MachineInit<G, U>,
    onUiChanged: (MachineKey<*, *>, Any) -> Unit
) : CommonStateMachine<G, U>, Activated {
    /**
     * Machine lifecycle
     */
    private val lifecycle = ActivatedLifecycleState(LifecycleState.State.PAUSED)

    /**
     * Currently working machine. Dummy while not started,
     */
    private var machine: ProxyStateMachine<G, U> = init.machine(lifecycle, onUiChanged)

    /**
     * Is active or not
     */
    override fun isActive(): Boolean = lifecycle.isActive()

    /**
     * Activates machine
     */
    override fun activate() {
        lifecycle.activate()
        machine.start()
    }

    /**
     * Deactivates machine
     */
    override fun deactivate() {
        lifecycle.deactivate()
    }

    override fun process(gesture: G) = machine.process(gesture)
    override fun clear() {
        if (machine.isStarted()) {
            machine.clear()
        }
    }
    override fun isStarted(): Boolean = machine.isStarted()
    override fun getUiState(): U = machine.getUiState()
    override fun setUiState(uiState: U) = machine.setUiState(uiState)
    override fun setMachineState(machineState: CommonMachineState<G, U>) = machine.setMachineState(machineState)
}

