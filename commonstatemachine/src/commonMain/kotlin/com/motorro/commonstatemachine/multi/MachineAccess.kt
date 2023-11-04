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

/**
 * Provides access to the state-machine
 * @param CG Child gesture system
 * @param CU Child UI-state system
 */
interface   MachineAccess<CG: Any, CU: Any> {
    /**
     * Keys collection
     */
    val keys: Set<MachineKey<out CG, out CU>>

    /**
     * Retrieves UI state.
     * [MachineInit] and [key] bind types securely.
     * @param U Concrete UI state bound with the [key], subtype of CU
     * @param key Machine key
     */
    fun <U: CU> getState(key: MachineKey<out CG, U>): U?

    /**
     * Processes machine gesture.
     * [MachineInit] and [key] bind types securely.
     * @param G Concrete gesture, subtype of th [CG]
     * @param key Machine key
     * @param gesture Gesture to process
     */
    fun <G: CG> process(key: MachineKey<G, out CU>, gesture: G)
}

/**
 * Retrieves a ui-state given the [MachineKey]
 */
interface UiStateProvider<CG: Any, CU: Any> {

    /**
     * Retrieves all running machine keys
     */
    fun getMachineKeys(): Set<MachineKey<out CG, out CU>>

    /**
     * Gets a concrete UI-state
     * @param key Machine key your state is bound to
     * @throws IllegalStateException if state is not found in common state
     */
    fun <U: CU> getValue(key: MachineKey<out CG, out U>): U = checkNotNull(get(key)) {
        "Key $key not found in machine map"
    }

    /**
     * Gets a concrete UI-state
     * @param key Machine key your state is bound to
     */
    operator fun <U: CU> get(key: MachineKey<out CG, U>): U?
}

/**
 * Redirects your gesture to be processed with a child machine
 * identified by [MachineKey]
 */
interface GestureProcessor<CG: Any, CU: Any> {
    /**
     * Redirects your gesture to be processed by child machine
     * if machine identified by [key] is found
     * @param key Machine key
     * @param gesture Gesture to process
     */
    fun <G: CG> process(key: MachineKey<G, out CU>, gesture: G)
}