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

/**
 * Proxy state that runs several machines at once
 * - Override [mapGesture] to pass gestures to the child machines
 * - Override [mapUiState] to build a combined UI state af all the machines
 * - Override [container] with a [ProxyMachineContainer] of your choice
 */
abstract class MultiMachineState<PG: Any, PU: Any> : CommonMachineState<PG, PU>() {
    /**
     * Proxy machines container
     */
    protected abstract val container: ProxyMachineContainer

    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        container.start(this::onUiStateChange)
    }

    /**
     * A part of [clear] template to clean-up state
     */
    override fun doClear() {
        container.clear()
    }

    /**
     * Updates machine view-state
     */
    @Suppress("UNUSED_PARAMETER")
    private fun onUiStateChange(key: MachineKey<out Any, out Any>, uiState: Any) {
        setUiState(buildUiState())
    }

    /**
     * Builds common UI state
     */
    private fun buildUiState(): PU {
        val machineMap = container.getMachines()
        val uiStateProvider = object : UiStateProvider {
            override fun getMachineKeys(): Set<MachineKey<*, *>> = machineMap.keys
            override fun <U : Any> get(key: MachineKey<*, U>): U? {
                return withMachine(key, machineMap) { getUiState() }
            }
        }
        return mapUiState(uiStateProvider)
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: PG) {
        val machineMap = container.getMachines()
        val processor = object : GestureProcessor {
            override fun <G : Any> process(key: MachineKey<G, *>, gesture: G) {
                withMachine(key, machineMap) { process(gesture) }
            }
        }
        mapGesture(gesture, processor)
    }

    /**
     * Updates child machines with gestures if relevant
     * @param parent Parent gesture
     * @param processor Use it to send child gesture to the relevant child machine
     */
    protected abstract fun mapGesture(parent: PG, processor: GestureProcessor)

    /**
     * Maps combined child UI state to parent
     * @param provider Provides child UI states
     */
    protected abstract fun mapUiState(provider: UiStateProvider): PU
}