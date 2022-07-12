package com.motorro.statemachine.machine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

/**
 * A common state-machine with live-data
 * @param G UI gesture
 * @param U UI state
 * @param init Initial state producer
 */
open class LiveDataStateMachine<G: Any, U: Any>(init: () -> CommonMachineState<G, U>): CommonStateMachine.Base<G, U>(init) {

    init {
        start()
    }

    /**
     * State mediator
     */
    private val mediator = MutableLiveData<U>()

    /**
     * Flow state
     */
    val uiState: LiveData<U> = Transformations.distinctUntilChanged(mediator)

    /**
     * Updates UI state
     * @param uiState UI state
     */
    final override fun setUiState(uiState: U) {
        mediator.value = uiState
    }
}