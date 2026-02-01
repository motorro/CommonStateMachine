package com.motorro.commonstatemachine.flow.viewmodel.data

/**
 * Wraps child UI state to adapt it to the hosting view component
 */
sealed class BaseFlowUiState<out U: Any, out R> {

    abstract val backHandlerEnabled: Boolean

    /**
     * Child UI state
     */
    data class Child<out U: Any>(val child: U, override val backHandlerEnabled: Boolean) : BaseFlowUiState<U, Nothing>()
    /**
     * Flow terminated
     */
    data class Terminated<out R>(val result: R?) : BaseFlowUiState<Nothing, R>() {
        override val backHandlerEnabled: Boolean = false
    }
}
