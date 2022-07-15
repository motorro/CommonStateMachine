package com.motorro.statemachine.welcome.model.state

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.ProxyMachineState
import com.motorro.statemachine.commonapi.data.RegistrationDataState
import com.motorro.statemachine.commonapi.model.state.RegistrationFeatureHost
import com.motorro.statemachine.login.data.LoginGesture
import com.motorro.statemachine.login.data.LoginUiState
import com.motorro.statemachine.login.di.LoginComponentBuilder
import com.motorro.statemachine.login.di.LoginEntryPoint
import com.motorro.statemachine.welcome.data.WelcomeGesture
import com.motorro.statemachine.welcome.data.WelcomeUiState
import dagger.hilt.EntryPoints
import dagger.hilt.android.scopes.ViewModelScoped
import timber.log.Timber
import javax.inject.Inject

/**
 * Proxy for login flow
 * Adapts login flow sub-graph to registration flow
 */
class LoginFlowState(
    private val context: WelcomeContext,
    private val data: RegistrationDataState,
    private val loginComponentBuilder: LoginComponentBuilder
) : LoginProxy(), RegistrationFeatureHost {
    /**
     * Creates initial child state
     */
    override fun init(): CommonMachineState<LoginGesture, LoginUiState> {
        val component = loginComponentBuilder.host(this).build()
        val factory = EntryPoints.get(component, LoginEntryPoint::class.java).factory()

        return factory.start(data)
    }

    /**
     * Maps child UI state to parent if relevant
     * @param parent Parent gesture
     * @return Mapped gesture or null if not applicable
     */
    override fun mapGesture(parent: WelcomeGesture): LoginGesture? = when (parent) {
        is WelcomeGesture.Login -> parent.value
        WelcomeGesture.Back -> LoginGesture.Back
        else -> null
    }

    /**
     * Maps child UI state to parent
     * @param child Child UI state
     */
    override fun mapUiState(child: LoginUiState): WelcomeUiState = WelcomeUiState.Login(child)

    /**
     * Returns user to email entry screen
     * @param data Common registration state data
     */
    override fun backToEmailEntry(data: RegistrationDataState) {
        Timber.d("Transferring to e-mail entry...")
        setMachineState(context.factory.emailEntry(data))
    }

    /**
     * Authentication complete
     * @param email Authenticated user's email
     */
    override fun complete(email: String) {
        Timber.d("Transferring to complete screen...")
        setMachineState(context.factory.complete(email))
    }

    @ViewModelScoped
    class Factory @Inject constructor(private val loginComponentBuilder: LoginComponentBuilder) {
        operator fun invoke(
            context: WelcomeContext,
            data: RegistrationDataState
        ): CommonMachineState<WelcomeGesture, WelcomeUiState> = LoginFlowState(
            context,
            data,
            loginComponentBuilder
        )
    }
}

private typealias LoginProxy = ProxyMachineState<WelcomeGesture, WelcomeUiState, LoginGesture, LoginUiState>
