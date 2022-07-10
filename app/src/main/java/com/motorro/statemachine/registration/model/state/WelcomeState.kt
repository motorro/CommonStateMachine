package com.motorro.statemachine.registration.model.state

import com.motorro.statemachine.R
import com.motorro.statemachine.registration.data.RegistrationGesture
import com.motorro.statemachine.registration.data.RegistrationUiState
import com.motorro.statemachine.resources.ResourceWrapper
import timber.log.Timber

/**
 * Welcome screen
 * Requires terms to be accepted before going next
 * Shares no common state
 */
class WelcomeState(
    context: RegistrationContext,
    resourceWrapper: ResourceWrapper
) : RegistrationState(context), ResourceWrapper by resourceWrapper {

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
    override fun doProcess(gesture: RegistrationGesture) = when(gesture) {
        RegistrationGesture.TermsAndConditionsToggled -> onTermsToggled()
        RegistrationGesture.Action -> onAction()
        RegistrationGesture.Back -> onBack()
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
            RegistrationUiState.Welcome(
                getString(R.string.welcome_terms),
                termsAccepted,
                termsAccepted
            )
        )
    }
}