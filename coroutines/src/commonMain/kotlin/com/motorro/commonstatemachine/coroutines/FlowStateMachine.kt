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

package com.motorro.commonstatemachine.coroutines

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.CommonStateMachine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach

/**
 * State machine that emits UI state as Flow
 * @param initialUiState Initial UI state
 */
open class FlowStateMachine<G: Any, U: Any>(initialUiState: U, init: () -> CommonMachineState<G, U>) : CommonStateMachine.Base<G, U>(init) {
    /**
     * State mediator
     */
    private val mediator: MutableStateFlow<U> = MutableStateFlow(initialUiState)

    init {
        start()
    }

    /**
     * Current UI state
     * @return current UI state or `null` if not yet available
     */
    override fun getUiState(): U = mediator.value

    /**
     * UI state
     */
    val uiState: StateFlow<U> = mediator

    /**
     * Subscription count of [uiState] to allow special actions on view connect/disconnect
     */
    val uiStateSubscriptionCount: StateFlow<Int> = mediator.subscriptionCount

    /**
     * Updates UI state
     * @param uiState UI state
     */
    final override fun setUiState(uiState: U) {
        mediator.tryEmit(uiState)
    }
}

/**
 * Watches UI-state subscriptions and updates state machine with gestures produced by [onActive]
 * and [onInactive].
 * May be used to suspend expensive operations in machine-states when no active subscribers
 * present on [FlowStateMachine.uiState]
 * @param scope Scope to run mapper
 * @param onActive Produces a gesture when view is active
 * @param onInactive Produces a gesture when view is inactive
 */
fun <G: Any, U: Any> FlowStateMachine<G, U>.mapUiSubscriptions(
    scope: CoroutineScope,
    onActive: (() -> G)? = null,
    onInactive: (() -> G)? = null
) {
    uiStateSubscriptionCount
        .map { count -> count > 0 }
        .distinctUntilChanged()
        .mapNotNull { active -> if (active) onActive?.invoke() else onInactive?.invoke() }
        .onEach(::process)
        .launchIn(scope)
}