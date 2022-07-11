package com.motorro.statemachine.registration.model.state

import com.motorro.statemachine.registration.data.RegistrationUiState
import timber.log.Timber

/**
 * Terminates wizard
 */
class TerminatedState(context: RegistrationContext) : RegistrationState(context) {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        Timber.d("Terminated")
        setUiState(RegistrationUiState.Terminated)
    }
}