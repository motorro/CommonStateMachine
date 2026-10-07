package com.motorro.commonstatemachine.examples.welcome.app.model.state

import androidx.lifecycle.SavedStateHandle
import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.examples.welcome.app.data.WelcomeDataState
import com.motorro.commonstatemachine.examples.welcome.app.data.WelcomeGesture
import com.motorro.commonstatemachine.examples.welcome.app.data.WelcomeUiState
import com.motorro.commonstatemachine.examples.welcome.app.model.WelcomeRenderer
import io.github.aakira.napier.Napier
import org.koin.core.annotation.Factory

/**
 * [WelcomeStateFactory] implementation
 */
@Factory
class WelcomeStateFactoryImpl(
    savedStateHandle: SavedStateHandle,
    renderer: WelcomeRenderer,
    private val createPreloading: PreloadingState.StateFactory,
    private val createLogin: LoginFlowState.StateFactory,
    private val createRegister: RegistrationFlowState.StateFactory,
    private val createEmailCheck: EmailCheckState.StateFactory
) : WelcomeStateFactory {

    private val context: WelcomeContext = object : WelcomeContext {
        override val factory = this@WelcomeStateFactoryImpl
        override val savedStateHandle = savedStateHandle
        override val renderer: WelcomeRenderer = renderer
    }

    /**
     * Preloads 'data' for registration
     */
    override fun preload(): CommonMachineState<WelcomeGesture, WelcomeUiState> {
        Napier.d("Creating 'Preloading'...")
        return createPreloading(context)
    }

    /**
     * Creates welcome screen state
     * @param welcomeGreeting Message to display on welcome screen
     */
    override fun welcome(welcomeGreeting: String): CommonMachineState<WelcomeGesture, WelcomeUiState> {
        Napier.d("Creating 'Welcome'...")
        return TermsAndConditionsState(context, welcomeGreeting)
    }

    /**
     * Creates email-entry state
     * @param data Data state
     */
    override fun emailEntry(data: WelcomeDataState?): CommonMachineState<WelcomeGesture, WelcomeUiState> {
        Napier.d("Creating 'Email entry'...")
        return EmailEntryState(context, data)
    }

    /**
     * Checks if email is registered
     * @param data Data state
     */
    override fun checkEmail(data: WelcomeDataState): CommonMachineState<WelcomeGesture, WelcomeUiState> {
        Napier.d("Creating 'Check e-mail'...")
        return createEmailCheck(context, data)
    }

    /**
     * Enter existing user password
     * @param data Data state
     */
    override fun loginFlow(data: WelcomeDataState): CommonMachineState<WelcomeGesture, WelcomeUiState> {
        Napier.d("Creating 'Login flow'...")
        return createLogin(context, data)
    }

    /**
     * Enter registration user password
     * @param data Data state
     */
    override fun registrationFlow(data: WelcomeDataState): CommonMachineState<WelcomeGesture, WelcomeUiState> {
        Napier.d("Creating 'Registration flow'...")
        return createRegister(context, data)
    }

    /**
     * Registration complete state
     * @param email Registered user's email
     */
    override fun complete(email: String): CommonMachineState<WelcomeGesture, WelcomeUiState> {
        Napier.d("Creating 'Complete'...")
        return CompleteState(context, email)
    }

    /**
     * Terminates registration flow
     */
    override fun terminate(): CommonMachineState<WelcomeGesture, WelcomeUiState> {
        Napier.d("Creating 'Terminated'...")
        return TerminatedState(context)
    }

}