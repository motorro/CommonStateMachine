package com.motorro.statemachine.registration.model.state

import androidx.lifecycle.SavedStateHandle
import com.motorro.statemachine.registration.data.RegistrationDataState
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
    fun preload(): RegistrationState

    /**
     * Creates welcome screen state
     * @param welcomeGreeting Message to display on welcome screen
     */
    fun welcome(welcomeGreeting: String): RegistrationState

    /**
     * Creates email-entry state
     * @param data Data state
     */
    fun emailEntry(data: RegistrationDataState? = null): RegistrationState

    /**
     * Checks if email is registered
     * @param data Data state
     */
    fun checkEmail(data: RegistrationDataState): RegistrationState

    /**
     * Terminates registration flow
     */
    fun terminate() : RegistrationState

    @ViewModelScoped
    class Impl @Inject constructor(
        savedStateHandle: SavedStateHandle,
        private val createPreloading: PreloadingState.Factory
    ) : RegistrationStateFactory {

        private val context: RegistrationContext = object : RegistrationContext {
            override val factory: RegistrationStateFactory = this@Impl
            override val savedStateHandle: SavedStateHandle = savedStateHandle
        }

        /**
         * Preloads 'data' for registration
         */
        override fun preload(): RegistrationState {
            Timber.d("Creating 'Preloading'...")
            return createPreloading(context)
        }

        /**
         * Creates welcome screen state
         * @param welcomeGreeting Message to display on welcome screen
         */
        override fun welcome(welcomeGreeting: String): RegistrationState {
            Timber.d("Creating 'Welcome'...")
            return WelcomeState(context, welcomeGreeting)
        }

        /**
         * Creates email-entry state
         * @param data Data state
         */
        override fun emailEntry(data: RegistrationDataState?): RegistrationState {
            Timber.d("Creating 'Email entry'...")
            return EmailEntryState(context, data)
        }

        /**
         * Checks if email is registered
         * @param data Data state
         */
        override fun checkEmail(data: RegistrationDataState): RegistrationState {
            TODO("Not yet implemented")
        }

        /**
         * Terminates registration flow
         */
        override fun terminate(): RegistrationState {
            Timber.d("Creating 'Terminated'...")
            return TerminatedState(context)
        }

    }
}