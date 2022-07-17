package com.motorro.statemachine.welcome.model.state

import com.motorro.statemachine.welcome.data.WelcomeGesture
import timber.log.Timber

class CompleteState(
    context: WelcomeContext,
    private val email: String
) : WelcomeState(context) {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        setUiState(renderer.renderComplete(email))
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: WelcomeGesture) = when(gesture) {
        WelcomeGesture.Back, WelcomeGesture.Action -> onBack()
        else -> super.doProcess(gesture)
    }

    private fun onBack() {
        Timber.d("Back. Terminating...")
        setMachineState(factory.terminate())
    }
}