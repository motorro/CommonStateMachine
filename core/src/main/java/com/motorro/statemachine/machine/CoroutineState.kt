package com.motorro.statemachine.machine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

/**
 * Base state class with coroutine support
 */
abstract class CoroutineState<G: Any, U: Any>: CommonMachineState<G, U>() {
    /**
     * Internal coroutine context bound to state lifecycle
     */
    protected val stateScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    /**
     * A part of [clear] template to clean-up state
     */
    override fun doClear() {
        stateScope.cancel()
    }
}