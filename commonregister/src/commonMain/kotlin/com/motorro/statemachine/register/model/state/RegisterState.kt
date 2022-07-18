package com.motorro.statemachine.register.model.state

import com.motorro.commonstatemachine.CoroutineState
import com.motorro.statemachine.commoncore.log.Logger
import com.motorro.statemachine.register.data.RegisterGesture
import com.motorro.statemachine.register.data.RegisterUiState

/**
 * Base class for registration flow state
 */
abstract class RegisterState(
    context: RegisterContext
): CoroutineState<RegisterGesture, RegisterUiState>(), RegisterContext by context {
    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: RegisterGesture) {
        Logger.w("Unsupported gesture: %s", gesture)
    }
}