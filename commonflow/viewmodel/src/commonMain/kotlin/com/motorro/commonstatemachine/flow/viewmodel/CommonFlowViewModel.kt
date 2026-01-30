package com.motorro.commonstatemachine.flow.viewmodel

import androidx.lifecycle.ViewModel
import com.motorro.commonstatemachine.ProxyMachineState
import com.motorro.commonstatemachine.coroutines.FlowStateMachine
import com.motorro.commonstatemachine.flow.data.CommonFlowDataApi
import com.motorro.commonstatemachine.flow.data.CommonFlowHost
import com.motorro.commonstatemachine.flow.viewmodel.data.BaseFlowGesture
import com.motorro.commonstatemachine.flow.viewmodel.data.BaseFlowUiState
import kotlinx.coroutines.flow.StateFlow

/**
 * Wraps child flow and adapts it to the base flow to use in standard views
 * @param G - gesture type
 * @param U - UI state type
 * @param I - input type
 * @param R - result type
 * @param api - flow data API
 * @param init - initial input
 * @param closeables the resources to be closed when the [ViewModel] is cleared, right **before** the [onCleared] method is called.
 * @see CommonFlowDataApi
 * @see CommonFlowHost
 */
open class CommonFlowViewModel<G: Any, U: Any, I, R>(
    private val api: CommonFlowDataApi<G, U, I, R>,
    private val init: I? = null,
    vararg closeables: AutoCloseable
) : ViewModel(*closeables) {

    companion object {
        /**
         * A cleanup key to associate with the state-machine
         */
        const val CLOSABLE_KEY = "CommonFlowStateMachine"
    }

    /**
     * Child flow proxy
     */
    private val stateMachine = FlowStateMachine(
        BaseFlowUiState.Child(
            child = api.getDefaultUiState(),
            backHandlerEnabled = null != api.getBackGesture()
        )
    ) {
        object : ProxyMachineState<BaseFlowGesture<G>, BaseFlowUiState<U, R>, G, U>(api.getDefaultUiState()) {

            private var terminated = false
            private val flowHost = object : CommonFlowHost<R> {
                override fun onComplete(result: R?) {
                    if (terminated.not()) {
                        terminated = true
                        setUiState(BaseFlowUiState.Terminated(result))
                    }
                }
            }

            override fun init() = api.init(flowHost, init)
            override fun mapGesture(parent: BaseFlowGesture<G>): G? = when(parent) {
                BaseFlowGesture.Back -> api.getBackGesture()
                is BaseFlowGesture.Child -> parent.child
            }
            override fun mapUiState(child: U): BaseFlowUiState<U, R> = BaseFlowUiState.Child(child, null != api.getBackGesture())
        }
    }

    init {
        addCloseable(CLOSABLE_KEY, AutoCloseable {
            stateMachine.clear()
        })
    }

    /**
     * Exported UI state
     */
    val uiState: StateFlow<BaseFlowUiState<U, R>> get() = stateMachine.uiState

    /**
     * Delegates gesture processing to the state-machine
     */
    fun process(gesture: BaseFlowGesture<G>) = stateMachine.process(gesture)
}