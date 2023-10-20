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

package com.motorro.commonstatemachine.lifecycle

import com.motorro.commonstatemachine.lifecycle.LifecycleState.State

/**
 * Provides some activity state to be able to shut-down
 * tasks like background monitoring while the host is inactive
 * and to resume when back in action.
 * E.g.: Application focus
 */
interface LifecycleState {

    /**
     * Activity state
     */
    fun getState(): State

    /**
     * True if something observes lifecycle
     */
    fun hasObservers(): Boolean

    /**
     * Adds state observer.
     * @param observer Observer to get state updates
     */
    fun addObserver(observer: Observer)

    /**
     * Removes state observer
     * @param observer Observer to get state updates
     */
    fun removeObserver(observer: Observer)

    /**
     * UI state
     */
    enum class State {
        /**
         * UI is paused
         */
        PAUSED,

        /**
         * UI is resumed
         */
        ACTIVE;
    }

    /**
     * State observer
     */
    fun interface Observer {
        /**
         * Called when state changes
         */
        fun onStateChange(state: State)
    }
}

/**
 * Combines parent (receiver) state with [child] to get the combined state with OR on [LifecycleState.State.PAUSED]:
 * - Parent active, child paused - PAUSED
 * - Parent paused, child paused - PAUSED
 * - Parent active, child active - ACTIVE
 */
fun LifecycleState.combinePaused(child: LifecycleState): LifecycleState = CombineLifecycleState(
    this,
    child,
    ::pausedIfNotAllActive
)

/**
 * Combines parent (receiver) state with [child] and [State.ACTIVE] if both are active:
 * - Parent active, child paused - PAUSED
 * - Parent paused, child paused - PAUSED
 * - Parent active, child active - ACTIVE
 */
private fun pausedIfNotAllActive(parent: State, child: State): State = when {
    State.ACTIVE == parent && State.ACTIVE == child -> State.ACTIVE
    else -> State.PAUSED
}

