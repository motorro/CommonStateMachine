package com.motorro.statemachine.welcome.model.state

import com.motorro.statemachine.welcome.data.WelcomeGesture
import com.motorro.statemachine.welcome.data.WelcomeUiState
import timber.log.Timber

/**
 * Terms and conditions screen
 * Requires terms to be accepted before going next
 * Shares no common state
 */
class TermsAndConditionsState(
    context: WelcomeContext,
    private val welcomeGreeting: String
) : WelcomeState(context) {

    /**
     * Simple internal state
     */
    private var termsAccepted: Boolean = false

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
        WelcomeGesture.TermsAndConditionsToggled -> onTermsToggled()
        WelcomeGesture.Action -> onAction()
        WelcomeGesture.Back -> onBack()
        else -> super.doProcess(gesture)
    }

    private fun onTermsToggled() {
        termsAccepted = !termsAccepted
        render()
    }

    private fun onAction() {
        if (termsAccepted) {
            Timber.d("Action. Going to email entry...")
            setMachineState(factory.emailEntry())
        }
    }

    private fun onBack() {
        Timber.d("Back. Terminating...")
        setMachineState(factory.terminate())
    }

    private fun render() {
        setUiState(
            WelcomeUiState.Welcome(
                welcomeGreeting,
                termsAccepted,
                termsAccepted
            )
        )
    }
}