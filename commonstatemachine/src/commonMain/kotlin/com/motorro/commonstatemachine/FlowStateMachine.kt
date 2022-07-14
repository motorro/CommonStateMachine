package com.motorro.commonstatemachine

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

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
    val uiState: SharedFlow<U> = mediator

    /**
     * Updates UI state
     * @param uiState UI state
     */
    final override fun setUiState(uiState: U) {
        mediator.tryEmit(uiState)
    }
}