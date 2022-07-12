package com.motorro.statemachine.registration.model.state

import com.motorro.statemachine.coroutines.DispatcherProvider
import com.motorro.statemachine.registration.data.RegistrationGesture
import com.motorro.statemachine.registration.data.RegistrationUiState
import com.motorro.statemachine.registrationapi.data.BAD
import com.motorro.statemachine.registrationapi.data.GOOD
import com.motorro.statemachine.registrationapi.data.RegistrationDataState
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * Switches login/registration flow based on hardcoded addresses
 */
class EmailCheckState(
    context: RegistrationContext,
    private val data: RegistrationDataState,
    private val dispatchers: DispatcherProvider
) : RegistrationState(context) {

    /**
     * Should have valid email at this point
     */
    private val email = requireNotNull(data.email) {
        "Email is not provided"
    }

    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        setUiState(RegistrationUiState.Loading)
        Timber.d("Checking if user exists...")
        stateScope.launch(dispatchers.default) {
            val exists = checkUserExists()
            withContext(dispatchers.main) {
                if (exists) {
                    Timber.d("Existing user. Transferring to login flow...")
                    setMachineState(factory.loginFlow(data))
                } else {
                    Timber.d("New user. Transferring to registration flow...")
                    setMachineState(factory.registrationFlow(data))
                }
            }
        }
    }

    /**
     * 'Checks' if user exists
     */
    private suspend fun checkUserExists(): Boolean {
        delay(2000L)
        return REGISTERED_USERS.contains(email.trim().lowercase())
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: RegistrationGesture) = when (gesture) {
        is RegistrationGesture.Back -> onBack()
        else -> super.doProcess(gesture)
    }

    /**
     * Exits app
     */
    private fun onBack() {
        Timber.d("Going back to email entry...")
        setMachineState(factory.emailEntry(data))
    }

    @ViewModelScoped
    class Factory @Inject constructor(private val dispatchers: DispatcherProvider) {
        operator fun invoke(
            context: RegistrationContext,
            data: RegistrationDataState
        ): RegistrationState = EmailCheckState(
            context,
            data,
            dispatchers
        )
    }

    companion object {
        /**
         * Registered users which switch to login flow
         */
        private val REGISTERED_USERS = setOf(GOOD, BAD)
    }
}