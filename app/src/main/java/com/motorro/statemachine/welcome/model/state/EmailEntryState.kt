package com.motorro.statemachine.welcome.model.state

import androidx.annotation.VisibleForTesting
import com.motorro.statemachine.welcome.data.WelcomeDataState
import com.motorro.statemachine.welcome.data.WelcomeGesture
import timber.log.Timber

/**
 * Email entry state
 * Passes common state to the next step
 * Uses saved state to restore data if available
 */
class EmailEntryState(
    context: WelcomeContext,
    inputData: WelcomeDataState?
) : WelcomeState(context) {

    /**
     * Local state data
     */
    private var data = processData(inputData)

    /**
     * Takes passed data
     * If no data passed - creates new instance and fills email from saved state if available
     */
    private fun processData(inputData: WelcomeDataState?) = when (inputData) {
        null -> WelcomeDataState(email = savedStateHandle.get(EMAIL_KEY))
        else -> inputData
    }

    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        render()
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: WelcomeGesture) = when(gesture) {
        WelcomeGesture.Action -> onAction()
        WelcomeGesture.Back -> onBack()
        is WelcomeGesture.EmailChanged -> onEmailChange(gesture)
        else -> super.doProcess(gesture)
    }

    private fun onAction() {
        if (data.isValid()) {
            Timber.d("Moving to checking email...")
            setMachineState(factory.checkEmail(data))
        }
    }

    private fun onBack() {
        Timber.d("Terminating...")
        setMachineState(factory.terminate())
    }

    private fun onEmailChange(gesture: WelcomeGesture.EmailChanged) {
        data = data.copy(email = gesture.value)
        render()
    }

    private fun render() {
        setUiState(renderer.renderEmailEntry(data, data.isValid()))
    }

    /**
     * State validation
     */
    private fun WelcomeDataState.isValid(): Boolean = EMAIL_REGEXP.matches(email.orEmpty())

    companion object {
        @VisibleForTesting()
        const val EMAIL_KEY = "email"

        private val EMAIL_REGEXP = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$".toRegex()
    }
}
