package com.motorro.statemachine.registration.model.state

import androidx.lifecycle.SavedStateHandle
import com.motorro.statemachine.machine.CommonMachineState
import com.motorro.statemachine.registration.data.RegistrationGesture
import com.motorro.statemachine.registration.data.RegistrationUiState
import com.motorro.statemachine.registrationapi.data.RegistrationDataState
import dagger.hilt.android.scopes.ViewModelScoped
import timber.log.Timber
import javax.inject.Inject

/**
 * Registration state factory
 */
interface RegistrationStateFactory {
    /**
     * Preloads 'data' for registration
     */
    fun preload(): CommonMachineState<RegistrationGesture, RegistrationUiState>

    /**
     * Creates welcome screen state
     * @param welcomeGreeting Message to display on welcome screen
     */
    fun welcome(welcomeGreeting: String): CommonMachineState<RegistrationGesture, RegistrationUiState>

    /**
     * Creates email-entry state
     * @param data Data state
     */
    fun emailEntry(data: RegistrationDataState? = null): CommonMachineState<RegistrationGesture, RegistrationUiState>

    /**
     * Checks if email is registered
     * @param data Data state
     */
    fun checkEmail(data: RegistrationDataState): CommonMachineState<RegistrationGesture, RegistrationUiState>

    /**
     * Enter existing user password
     * @param data Data state
     */
    fun loginFlow(data: RegistrationDataState): CommonMachineState<RegistrationGesture, RegistrationUiState>

    /**
     * Enter registration user password
     * @param data Data state
     */
    fun registrationFlow(data: RegistrationDataState): CommonMachineState<RegistrationGesture, RegistrationUiState>

    /**
     * Registration complete state
     * @param email Registered user's email
     */
    fun complete(email: String) : CommonMachineState<RegistrationGesture, RegistrationUiState>

    /**
     * Terminates registration flow
     */
    fun terminate() : CommonMachineState<RegistrationGesture, RegistrationUiState>

    /**
     * [RegistrationStateFactory] implementation
     */
    @ViewModelScoped
    class Impl @Inject constructor(
        savedStateHandle: SavedStateHandle,
        private val createPreloading: PreloadingState.Factory,
        private val createLogin: LoginFlowState.Factory,
        private val createEmailCheck: EmailCheckState.Factory
    ) : RegistrationStateFactory {

        private val context: RegistrationContext = object : RegistrationContext {
            override val factory = this@Impl
            override val savedStateHandle = savedStateHandle
        }

        /**
         * Preloads 'data' for registration
         */
        override fun preload(): CommonMachineState<RegistrationGesture, RegistrationUiState> {
            Timber.d("Creating 'Preloading'...")
            return createPreloading(context)
        }

        /**
         * Creates welcome screen state
         * @param welcomeGreeting Message to display on welcome screen
         */
        override fun welcome(welcomeGreeting: String): CommonMachineState<RegistrationGesture, RegistrationUiState> {
            Timber.d("Creating 'Welcome'...")
            return WelcomeState(context, welcomeGreeting)
        }

        /**
         * Creates email-entry state
         * @param data Data state
         */
        override fun emailEntry(data: RegistrationDataState?): CommonMachineState<RegistrationGesture, RegistrationUiState> {
            Timber.d("Creating 'Email entry'...")
            return EmailEntryState(context, data)
        }

        /**
         * Checks if email is registered
         * @param data Data state
         */
        override fun checkEmail(data: RegistrationDataState): CommonMachineState<RegistrationGesture, RegistrationUiState> {
            Timber.d("Creating 'Check e-mail'...")
            return createEmailCheck(context, data)
        }

        /**
         * Enter existing user password
         * @param data Data state
         */
        override fun loginFlow(data: RegistrationDataState): CommonMachineState<RegistrationGesture, RegistrationUiState> {
            Timber.d("Creating 'Login flow'...")
            return createLogin(context, data)
        }

        /**
         * Enter registration user password
         * @param data Data state
         */
        override fun registrationFlow(data: RegistrationDataState): CommonMachineState<RegistrationGesture, RegistrationUiState> {
            TODO("Not yet implemented")
        }

        /**
         * Registration complete state
         * @param email Registered user's email
         */
        override fun complete(email: String): CommonMachineState<RegistrationGesture, RegistrationUiState> {
            TODO("Not yet implemented")
        }

        /**
         * Terminates registration flow
         */
        override fun terminate(): CommonMachineState<RegistrationGesture, RegistrationUiState> {
            Timber.d("Creating 'Terminated'...")
            return TerminatedState(context)
        }

    }
}