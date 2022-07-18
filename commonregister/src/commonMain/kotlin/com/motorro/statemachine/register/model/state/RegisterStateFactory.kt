package com.motorro.statemachine.register.model.state

import com.motorro.statemachine.commonapi.welcome.data.WelcomeDataState
import com.motorro.statemachine.commonapi.welcome.model.state.WelcomeFeatureHost
import com.motorro.statemachine.commoncore.log.Logger
import com.motorro.statemachine.register.data.RegisterDataState
import com.motorro.statemachine.register.model.RegistrationRenderer

/**
 * Login flow state factory
 */
interface RegisterStateFactory {
    /**
     * Creates a starting state
     * @param data Common data state
     */
    fun start(data: WelcomeDataState): RegisterState = passwordEntry(RegisterDataState(data))

    /**
     * Password entry screen
     * @param data Registration data state
     */
    fun passwordEntry(data: RegisterDataState): RegisterState

    /**
     * Registers user
     * @param data Data state
     */
    fun registering(data: RegisterDataState): RegisterState

    /**
     * [RegisterStateFactory] implementation
     */
    class Impl(
        host: WelcomeFeatureHost,
        renderer: RegistrationRenderer,
        private val createRegistration: RegistrationState.Factory,
    ) : RegisterStateFactory {

        private val context: RegisterContext = object : RegisterContext {
            override val factory: RegisterStateFactory = this@Impl
            override val host: WelcomeFeatureHost = host
            override val renderer: RegistrationRenderer = renderer
        }

        /**
         * Password entry screen
         * @param data Registration data state
         */
        override fun passwordEntry(data: RegisterDataState): RegisterState {
            Logger.d("Creating 'Password entry'...")
            return PasswordEntryState(context, data)
        }

        /**
         * Registers user
         * @param data Data state
         */
        override fun registering(data: RegisterDataState): RegisterState {
            Logger.d("Creating 'Credentials check'...")
            return createRegistration(context, data)
        }
    }
}