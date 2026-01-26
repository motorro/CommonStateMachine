package com.motorro.commonstatemachine.flow

import com.motorro.commonstatemachine.CommonMachineState

/**
 * Common flow data API
 */
interface CommonFlowDataApi<G: Any, U: Any, I, R, F : CommonFlowHost<R>> {
    /**
     * Creates flow
     */
    fun init(flowHost: F, input: I? = null): CommonMachineState<G, U>

    /**
     * Returns default UI state
     */
    fun getDefaultUiState(): U

    /**
     * Returns back gesture mapping
     */
    fun getBackGesture(): G? = null
}