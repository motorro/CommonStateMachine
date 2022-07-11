package com.motorro.statemachine.registration.model.state

import androidx.lifecycle.SavedStateHandle
import com.motorro.statemachine.machine.CommonMachineState
import com.motorro.statemachine.registration.data.RegistrationGesture
import com.motorro.statemachine.registration.data.RegistrationUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import timber.log.Timber

/**
 * Base class for registration flow state
 */
abstract class RegistrationState(private val context: RegistrationContext): CommonMachineState<RegistrationGesture, RegistrationUiState>() {
    /**
     * State factory
     */
    protected val factory: RegistrationStateFactory
        get() = context.factory

    /**
     * Saved state handle
     */
    protected val savedStateHandle: SavedStateHandle
        get() = context.savedStateHandle

    /**
     * Internal coroutine context bound to state lifecycle
     */
    protected val stateScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: RegistrationGesture) {
        Timber.w("Unsupported gesture: %s", gesture)
    }

    /**
     * A part of [clear] template to clean-up state
     */
    override fun doClear() {
        stateScope.cancel()
    }
}