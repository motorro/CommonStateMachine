package com.motorro.statemachine.registration.model.state

import com.motorro.statemachine.registration.data.RegistrationGesture
import com.motorro.statemachine.registration.data.RegistrationUiState
import timber.log.Timber

class CompleteState(
    context: RegistrationContext,
    private val email: String
) : RegistrationState(context) {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        setUiState(RegistrationUiState.Complete(email))
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: RegistrationGesture) = when(gesture) {
        RegistrationGesture.Back, RegistrationGesture.Action -> onBack()
        else -> super.doProcess(gesture)
    }

    private fun onBack() {
        Timber.d("Back. Terminating...")
        setMachineState(factory.terminate())
    }
}