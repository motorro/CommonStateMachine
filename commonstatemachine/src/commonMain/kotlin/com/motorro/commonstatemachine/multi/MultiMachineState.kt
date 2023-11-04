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
abstract class MultiMachineState<PG: Any, PU: Any, CG: Any, CU: Any> : CommonMachineState<PG, PU>() {
    /**
     * Proxy machines container
     */
    protected abstract val container: ProxyMachineContainer<CG, CU>

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
    private fun onUiStateChange(key: MachineKey<out CG, out CU>, uiState: CU) {
        setUiState(buildUiState(key))
    }

    /**
     * Builds common UI state
     */
    private fun buildUiState(changedKey: MachineKey<out CG, out CU>?): PU {
        val access = container.machineAccess
        val uiStateProvider = object : UiStateProvider<CG, CU> {
            override fun getMachineKeys(): Set<MachineKey<out CG, out CU>> = access.keys
            override fun <U : CU> get(key: MachineKey<out CG, U>): U? = access.getState(key)
        }
        return mapUiState(uiStateProvider, changedKey)
    }

    /**
     * Updates hosting machine with composed UI state
     */
    protected fun updateUi() {
        setUiState(buildUiState(null))
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: PG) {
        val access = container.machineAccess
        val processor = object : GestureProcessor<CG, CU> {
            override fun <G : CG> process(key: MachineKey<G, out CU>, gesture: G) {
                access.process(key, gesture)
            }
        }
        mapGesture(gesture, processor)
    }

    /**
     * Updates child machines with gestures if relevant
     * @param parent Parent gesture
     * @param processor Use it to send child gesture to the relevant child machine
     */
    protected abstract fun mapGesture(parent: PG, processor: GestureProcessor<CG, CU>)

    /**
     * Maps combined child UI state to parent
     * @param provider Provides child UI states
     * @param changedKey Key of machine that changed the UI state. Null if called explicitly via [updateUi]
     * @see updateUi
     */
    protected abstract fun mapUiState(provider: UiStateProvider<CG, CU>, changedKey: MachineKey<out CG, out CU>?): PU
}