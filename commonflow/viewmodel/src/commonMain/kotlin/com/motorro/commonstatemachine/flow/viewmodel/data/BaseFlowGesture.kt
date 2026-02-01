package com.motorro.commonstatemachine.flow.viewmodel.data

/**
 * Wraps child gesture to adapt it to the hosting view component
 */
sealed class BaseFlowGesture<out G: Any> {
    /**
     * Back navigation gesture
     */
    data object Back : BaseFlowGesture<Nothing>()

    /**
     * Child flow gesture
     */
    data class Child<G: Any>(val child: G) : BaseFlowGesture<G>()
}
