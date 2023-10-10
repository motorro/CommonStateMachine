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

import com.motorro.commonstatemachine.CommonStateMachine

/**
 * Retrieves a ui-state given the [MachineKey]
 */
interface UiStateProvider {

    /**
     * Retrieves all running machine keys
     */
    fun getMachineKeys(): Set<MachineKey<*, *>>

    /**
     * Gets a concrete UI-state
     * @param key Machine key your state is bound to
     * @throws IllegalStateException if state is not found in common state
     */
    fun <U: Any> getValue(key: MachineKey<*, U>): U = checkNotNull(get(key)) {
        "Key $key not found in machine map"
    }

    /**
     * Gets a concrete UI-state
     * @param key Machine key your state is bound to
     */
    fun <U: Any> get(key: MachineKey<*, U>): U?
}

/**
 * Redirects your gesture to be processed with a child machine
 * identified by [MachineKey]
 */
interface GestureProcessor {
    /**
     * Redirects your gesture to be processed by child machine
     * if machine identified by [key] is found
     * @param key Machine key
     * @param gesture Gesture to process
     */
    fun <G: Any> process(key: MachineKey<G, *>, gesture: G)
}

/**
 * Runs [block] with machine stored in [machineMap] under this key
 */
@Suppress("UNCHECKED_CAST")
internal inline fun <G: Any, U: Any, R> withMachine(
    key: MachineKey<G, U>,
    machineMap: MachineMap,
    block: CommonStateMachine<G, U>.() -> R
): R? = (machineMap[key] as? CommonStateMachine<G, U>)?.block()

