package com.motorro.statemachine.registration.model.state

import androidx.annotation.VisibleForTesting
import com.motorro.statemachine.registration.data.RegistrationGesture
import com.motorro.statemachine.registration.data.RegistrationUiState
import com.motorro.statemachine.registrationapi.data.RegistrationDataState
import timber.log.Timber

/**
 * Email entry state
 * Passes common state to the next step
 * Uses saved state to restore data if available
 */
class EmailEntryState(
    context: RegistrationContext,
    inputData: RegistrationDataState?
) : RegistrationState(context) {

    /**
     * Local state data
     */
    private var data = processData(inputData)

    /**
     * Takes passed data
     * If no data passed - creates new instance and fills email from saved state if available
     */
    private fun processData(inputData: RegistrationDataState?) = when (inputData) {
        null -> RegistrationDataState(email = savedStateHandle.get(EMAIL_KEY))
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
    override fun doProcess(gesture: RegistrationGesture) = when(gesture) {
        RegistrationGesture.Action -> onAction()
        RegistrationGesture.Back -> onBack()
        is RegistrationGesture.EmailChanged -> onEmailChange(gesture)
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

    private fun onEmailChange(gesture: RegistrationGesture.EmailChanged) {
        data = data.copy(email = gesture.value)
        render()
    }

    private fun render() {
        setUiState(
            RegistrationUiState.EmailEntry(
                data.email.orEmpty(),
                data.isValid()
            )
        )
    }

    /**
     * State validation
     */
    private fun RegistrationDataState.isValid(): Boolean = EMAIL_REGEXP.matches(email.orEmpty())

    companion object {
        @VisibleForTesting()
        const val EMAIL_KEY = "email"

        private val EMAIL_REGEXP = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$".toRegex()
    }
}
