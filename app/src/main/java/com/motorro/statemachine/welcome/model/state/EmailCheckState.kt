package com.motorro.statemachine.welcome.model.state

import com.motorro.statemachine.commonapi.data.BAD
import com.motorro.statemachine.commonapi.data.GOOD
import com.motorro.statemachine.commonapi.data.RegistrationDataState
import com.motorro.statemachine.coroutines.DispatcherProvider
import com.motorro.statemachine.welcome.data.WelcomeGesture
import com.motorro.statemachine.welcome.data.WelcomeUiState
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
    context: WelcomeContext,
    private val data: RegistrationDataState,
    private val dispatchers: DispatcherProvider
) : WelcomeState(context) {

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
        setUiState(WelcomeUiState.Loading)
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
    override fun doProcess(gesture: WelcomeGesture) = when (gesture) {
        is WelcomeGesture.Back -> onBack()
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
            context: WelcomeContext,
            data: RegistrationDataState
        ): WelcomeState = EmailCheckState(
            context,
            data,
            dispatchers
        )
    }

    companion object {
        /**
         * Registered users which switch to login flow
         */
        private val REGISTERED_USERS = setOf(
            GOOD,
            BAD
        )
    }
}