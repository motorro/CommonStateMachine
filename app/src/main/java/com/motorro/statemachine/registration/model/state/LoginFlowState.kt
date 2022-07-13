package com.motorro.statemachine.registration.model.state

import com.motorro.statemachine.login.data.LoginGesture
import com.motorro.statemachine.login.data.LoginUiState
import com.motorro.statemachine.login.di.LoginComponentBuilder
import com.motorro.statemachine.login.di.LoginEntryPoint
import com.motorro.statemachine.machine.CommonMachineState
import com.motorro.statemachine.machine.ProxyMachineState
import com.motorro.statemachine.registration.data.RegistrationGesture
import com.motorro.statemachine.registration.data.RegistrationUiState
import com.motorro.statemachine.registrationapi.data.RegistrationDataState
import com.motorro.statemachine.registrationapi.model.state.RegistrationFeatureHost
import dagger.hilt.EntryPoints
import dagger.hilt.android.scopes.ViewModelScoped
import timber.log.Timber
import javax.inject.Inject

/**
 * Proxy for login flow
 * Adapts login flow sub-graph to registration flow
 */
class LoginFlowState(
    private val context: RegistrationContext,
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
    override fun mapGesture(parent: RegistrationGesture): LoginGesture? = when (parent) {
        is RegistrationGesture.Login -> parent.value
        else -> null
    }

    /**
     * Maps child UI state to parent
     * @param child Child UI state
     */
    override fun mapUiState(child: LoginUiState): RegistrationUiState = RegistrationUiState.Login(child)

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
            context: RegistrationContext,
            data: RegistrationDataState
        ): CommonMachineState<RegistrationGesture, RegistrationUiState> = LoginFlowState(
            context,
            data,
            loginComponentBuilder
        )
    }
}

private typealias LoginProxy = ProxyMachineState<RegistrationGesture, RegistrationUiState, LoginGesture, LoginUiState>
