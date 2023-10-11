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
 * Common state machine input - from the outside world to the current state
 * @param G UI gesture
 */
interface MachineInput<G: Any> {
    /**
     * Updates state with UI gesture
     * @param gesture UI gesture to proceed
     */
    fun process(gesture: G)

    /**
     * Cleans-up and shuts down the state-machine
     */
    fun clear()
}

/**
 * Common state machine output - from the current state to the outside world
 * @param U UI state
 */
interface MachineOutput<G: Any, U: Any> {
    /**
     * Sets active machine state
     * @param machineState State to transition to
     */
    fun setMachineState(machineState: CommonMachineState<G, U>)

    /**
     * Updates UI state
     * @param uiState UI state
     */
    fun setUiState(uiState: U)
}

/**
 * Current public machine status
 */
interface MachineStatus<U : Any> {
    /**
     * Checks if machine is started
     */
    fun isStarted(): Boolean

    /**
     * Current UI state
     * @return current UI state or `null` if not yet available
     */
    fun getUiState(): U
}

/**
 * Common state machine
 * @param G UI gesture
 * @param U UI state
 */
interface CommonStateMachine<G: Any, U: Any> : MachineInput<G>, MachineOutput<G, U>, MachineStatus<U> {

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
         * Checks if machine is started
         */
        final override fun isStarted(): Boolean = started

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
        fun start() {
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
        override fun clear() {
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

