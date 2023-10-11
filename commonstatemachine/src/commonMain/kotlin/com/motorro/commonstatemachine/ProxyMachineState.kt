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

/**
 * Proxy state that runs child state machine and maps gestures and results to parent
 * Use when you want to plug-in some child sub-graph of states to parent
 * @param PG Parent gesture
 * @param PU Parent UI state
 * @param CG Child gesture
 * @param CU Child UI state
 * @param initialChildUiState Initial child UI state
 * @see mapUiState
 * @see mapGesture
 */
abstract class ProxyMachineState<PG: Any, PU: Any, CG: Any, CU: Any>(initialChildUiState: CU) : CommonMachineState<PG, PU>() {
    /**
     * Proxy state machine
     */
    private val machine = ProxyStateMachine(initialChildUiState, ::init) {
        setUiState(mapUiState(it))
    }

    /**
     * Returns child machine state
     */
    protected fun getChildMachineState(): MachineStatus<CU> = machine

    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        machine.start()
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: PG) {
        mapGesture(gesture)?.let(machine::process)
    }

    /**
     * A part of [clear] template to clean-up state
     */
    override fun doClear() {
        machine.clear()
    }

    /**
     * Creates initial child state
     */
    protected abstract fun init(): CommonMachineState<CG, CU>

    /**
     * Maps parent UI state to child if relevant
     * @param parent Parent gesture
     * @return Mapped gesture or null if not applicable
     */
    protected abstract fun mapGesture(parent: PG): CG?

    /**
     * Maps child UI state to parent
     * @param child Child UI state
     */
    protected abstract fun mapUiState(child: CU): PU
}