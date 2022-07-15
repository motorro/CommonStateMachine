package com.motorro.statemachine.login.model.state

import com.motorro.statemachine.commonapi.coroutines.DispatcherProvider
import com.motorro.statemachine.login.data.LoginDataState
import com.motorro.statemachine.login.data.LoginGesture
import com.motorro.statemachine.login.data.LoginUiState
import com.motorro.statemachine.login.di.LoginScope
import com.motorro.statemachine.welcome.data.BAD
import com.motorro.statemachine.welcome.data.GOOD
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * Emulates login operation
 */
class CredentialsCheckState(
    context: LoginContext,
    private val data: LoginDataState,
    private val dispatchers: DispatcherProvider
) : LoginState(context) {

    /**
     * Should have valid email at this point
     */
    private val email = requireNotNull(data.commonData.email) {
        "Email is not provided"
    }

    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        setUiState(LoginUiState.Loading)
        Timber.d("Checking for user credentials...")
        stateScope.launch(dispatchers.default) {
            val exists = checkCredentials()
            withContext(dispatchers.main) {
                if (exists) {
                    Timber.d("Correct credentials. Transferring to complete screen...")
                    host.complete(email)
                } else {
                    Timber.w("Login error. Transferring to error screen...")
                    setMachineState(factory.error(data, IllegalArgumentException("Wrong username or password")))
                }
            }
        }
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: LoginGesture) = when(gesture) {
        LoginGesture.Back -> onBack()
        else -> super.doProcess(gesture)
    }

    private fun onBack() {
        Timber.d("Returning to password entry")
        setMachineState(factory.passwordEntry(data))
    }

    /**
     * 'Checks' if user exists
     */
    private suspend fun checkCredentials(): Boolean {
        delay(2000L)
        return true == REGISTERED_USERS[email.trim().lowercase()]
    }

    @LoginScope
    class Factory @Inject constructor(private val dispatchers: DispatcherProvider) {
        operator fun invoke(
            context: LoginContext,
            data: LoginDataState
        ): LoginState = CredentialsCheckState(
            context,
            data,
            dispatchers
        )
    }

    companion object {
        /**
         * Users than can or can not login
         * Any password will do
         */
        private val REGISTERED_USERS = mapOf(
            BAD to false,
            GOOD to true
        )
    }
}