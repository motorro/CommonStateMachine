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
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

/**
 * State machine that emits UI state as Flow
 */
open class FlowStateMachine<G: Any, U: Any>(init: () -> CommonMachineState<G, U>) : CommonStateMachine.Base<G, U>(init) {
    /**
     * State mediator
     */
    private val mediator = MutableSharedFlow<U>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    init {
        start()
    }

    /**
     * UI state
     */
    val uiState: SharedFlow<U> = mediator.asSharedFlow()

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