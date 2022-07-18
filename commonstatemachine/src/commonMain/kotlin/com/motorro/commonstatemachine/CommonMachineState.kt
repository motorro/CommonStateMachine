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
 * Common state-machine state
 * @param G UI gesture
 * @param U UI state
 */
open class CommonMachineState<G: Any, U : Any> {
    /**
     * Hosting state machine
     */
    private var machine: CommonStateMachine<G, U>? = null

    /**
     * Starts state
     */
    fun start(machine: CommonStateMachine<G, U>) {
        this.machine = machine
        doStart()
    }

    /**
     * A part of [start] template to initialize state
     */
    protected open fun doStart(): Unit = Unit

    /**
     * Updates state with UI gesture
     * @param gesture UI gesture to proceed
     */
    fun process(gesture: G): Unit = withMachine {
        doProcess(gesture)
    }

    /**
     * A part of [process] template to process UI gesture
     */
    protected open fun doProcess(gesture: G): Unit = Unit

    /**
     * Updates UI state
     * @param uiState UI state
     */
    protected fun setUiState(uiState: U) = withMachine {
        it.setUiState(uiState)
    }

    /**
     * Updates machine state
     * @param machineState Machine state
     */
    protected fun setMachineState(machineState: CommonMachineState<G, U>) = withMachine {
        it.setMachineState(machineState)
    }

    /**
     * Ensures machine is set
     * @param block Block to run on machine
     */
    private inline fun withMachine(block: (CommonStateMachine<G, U>) -> Unit) {
        val machine = checkNotNull(machine) { "Can't update Machine while not active" }
        block(machine)
    }

    /**
     * Called when state is removed from machine
     * Cleans-up the state
     */
    fun clear() {
        doClear()
        machine = null
    }

    /**
     * A part of [clear] template to clean-up state
     */
    protected open fun doClear(): Unit = Unit
}