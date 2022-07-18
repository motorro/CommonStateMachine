package com.motorro.statemachine.login.model.state

import com.motorro.statemachine.commonapi.welcome.data.WelcomeDataState
import com.motorro.statemachine.commonapi.welcome.model.state.WelcomeFeatureHost
import com.motorro.statemachine.login.data.LoginDataState
import com.motorro.statemachine.login.di.LoginScope
import com.motorro.statemachine.login.model.LoginRenderer
import timber.log.Timber
import javax.inject.Inject

/**
 * Login flow state factory
 */
interface LoginStateFactory {
    /**
     * Creates a starting state
     * @param data Common data state
     */
    fun start(data: WelcomeDataState): LoginState = passwordEntry(LoginDataState(data))

    /**
     * Enter existing user password
     * @param data Login data state
     */
    fun passwordEntry(data: LoginDataState): LoginState

    /**
     * Checks email/password
     * @param data Data state
     */
    fun checking(data: LoginDataState): LoginState

    /**
     * Password error screen
     */
    fun error(data: LoginDataState, error: Throwable): LoginState

    /**
     * [LoginStateFactory] implementation
     */
    @LoginScope
    class Impl @Inject constructor(
        host: WelcomeFeatureHost,
        renderer: LoginRenderer,
        private val createCredentialsCheck: CredentialsCheckState.Factory
    ) : LoginStateFactory {

        private val context: LoginContext = object : LoginContext {
            override val factory: LoginStateFactory = this@Impl
            override val host: WelcomeFeatureHost = host
            override val renderer: LoginRenderer = renderer
        }

        /**
         * Enter existing user password
         * @param data Common data state
         */
        override fun passwordEntry(data: LoginDataState): LoginState {
            Timber.d("Creating 'Password entry'...")
            return PasswordEntryState(context, data)
        }

        /**
         * Checks email/password
         * @param data Data state
         */
        override fun checking(data: LoginDataState): LoginState {
            Timber.d("Creating 'Credentials check'...")
            return createCredentialsCheck(context, data)
        }

        /**
         * Password error screen
         */
        override fun error(data: LoginDataState, error: Throwable): LoginState {
            Timber.d("Creating 'Error'...")
            return ErrorState(context, data, error)
        }
    }
}