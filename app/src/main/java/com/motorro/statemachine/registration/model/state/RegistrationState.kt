package com.motorro.statemachine.registration.model.state

import com.motorro.statemachine.machine.CommonMachineState
import com.motorro.statemachine.registration.data.RegistrationGesture
import com.motorro.statemachine.registration.data.RegistrationUiState
import timber.log.Timber

/**
 * Base class for registration flow state
 */
abstract class RegistrationState(protected val context: RegistrationContext): CommonMachineState<RegistrationGesture, RegistrationUiState>() {
    /**
     * State factory
     */
    protected val factory: RegistrationStateFactory
        get() = context.factory

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: RegistrationGesture) {
        Timber.w("Unsupported gesture: %s", gesture)
    }
}