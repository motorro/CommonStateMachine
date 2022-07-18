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
 * Common state machine
 * @param G UI gesture
 * @param U UI state
 */
interface CommonStateMachine<G: Any, U: Any> {
    /**
     * Sets active machine state
     */
    fun setMachineState(machineState: CommonMachineState<G, U>)

    /**
     * Updates state with UI gesture
     * @param gesture UI gesture to proceed
     */
    fun process(gesture: G)

    /**
     * Updates UI state
     * @param uiState UI state
     */
    fun setUiState(uiState: U)

    /**
     * Cleans-up state-machine
     */
    fun clear()

    /**
     * Base state-machine implementation
     * @param G UI gesture
     * @param U UI state
     * @param init Initial state producer
     */
    abstract class Base<G: Any, U: Any>(private val init: () -> CommonMachineState<G, U>) : CommonStateMachine<G, U> {

        /**
         * Active machine state
         */
        protected lateinit var activeState: CommonMachineState<G, U>

        /**
         * Start fuze
         */
        private var started: Boolean = false

        /**
         * Sets active machine state
         */
        final override fun setMachineState(machineState: CommonMachineState<G, U>) {
            clear()
            activeState = machineState
            startMachineState()
        }

        /**
         * Starts machine
         */
        protected fun start() {
            if (started.not()) {
                activeState = init()
                startMachineState()
                started = true
            }
        }

        /**
         * Updates state with UI gesture
         * @param gesture UI gesture to proceed
         */
        final override fun process(gesture: G) {
            activeState.process(gesture)
        }

        /**
         * Cleans-up state-machine
         */
        final override fun clear() {
            activeState.clear()
        }

        /**
         * Starts machine state
         */
        private fun startMachineState() {
            activeState.start(this)
        }
    }
}

