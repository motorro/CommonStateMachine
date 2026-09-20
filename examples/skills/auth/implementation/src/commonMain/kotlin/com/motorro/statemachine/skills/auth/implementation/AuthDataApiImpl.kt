package com.motorro.statemachine.skills.auth.implementation

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.flow.data.CommonFlowHost
import com.motorro.statemachine.skills.auth.api.AuthDataApi
import com.motorro.statemachine.skills.auth.api.AuthGesture
import com.motorro.statemachine.skills.auth.api.AuthInput
import com.motorro.statemachine.skills.auth.api.AuthResult
import com.motorro.statemachine.skills.auth.api.AuthUiState
import com.motorro.statemachine.skills.auth.implementation.data.AuthGestureImpl
import com.motorro.statemachine.skills.auth.implementation.data.AuthUiStateImpl
import com.motorro.statemachine.skills.auth.implementation.state.AuthStateFactory
import org.koin.core.annotation.Factory
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

/**
 * Data API implementation
 */
@Factory
internal class AuthDataApiImpl : AuthDataApi, KoinComponent {

    @Suppress("UNCHECKED_CAST")
    override fun init(flowHost: CommonFlowHost<AuthResult>, input: AuthInput): CommonMachineState<AuthGesture, AuthUiState> {
        val factory = this@AuthDataApiImpl.get<AuthStateFactory> {
            parametersOf(flowHost)
        }
        return factory.preloading(input) as CommonMachineState<AuthGesture, AuthUiState>
    }

    override fun getDefaultUiState(): AuthUiState = AuthUiStateImpl.Loading

    override fun getBackGesture(): AuthGesture = AuthGestureImpl.Back
}