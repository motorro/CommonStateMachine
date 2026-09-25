/*
 * Copyright 2022 Nikolai Kotchetkov.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.motorro.commonstatemachine.examples.welcome.app.model.state

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.ProxyMachineState
import com.motorro.commonstatemachine.examples.welcome.app.data.WelcomeDataState
import com.motorro.commonstatemachine.examples.welcome.app.data.WelcomeGesture
import com.motorro.commonstatemachine.examples.welcome.app.data.WelcomeUiState
import com.motorro.commonstatemachine.examples.welcome.commonapi.model.state.WelcomeFeatureHost
import com.motorro.commonstatemachine.examples.welcome.register.RegisterFlowStarter
import com.motorro.commonstatemachine.examples.welcome.register.data.RegisterGesture
import com.motorro.commonstatemachine.examples.welcome.register.data.RegisterUiState
import io.github.aakira.napier.Napier
import org.koin.core.annotation.Factory
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

/**
 * Proxy for registration flow
 * Adapts registration flow sub-graph to welcome flow
 */
class RegistrationFlowState(
    private val context: WelcomeContext,
    private val data: WelcomeDataState,
    private val startRegister: (WelcomeFeatureHost, String) -> CommonMachineState<RegisterGesture, RegisterUiState>
) : RegistrationProxy(RegisterUiState.Loading), WelcomeFeatureHost {

    /**
     * Should have valid email at this point
     */
    private val email = requireNotNull(data.email) {
        "Email is not provided"
    }

    /**
     * Creates initial child state
     */
    override fun init(): CommonMachineState<RegisterGesture, RegisterUiState>  = startRegister(this, email)

    /**
     * Maps child UI state to parent if relevant
     * @param parent Parent gesture
     * @return Mapped gesture or null if not applicable
     */
    override fun mapGesture(parent: WelcomeGesture): RegisterGesture? = when (parent) {
        is WelcomeGesture.Register -> parent.value
        WelcomeGesture.Back -> RegisterGesture.Back
        else -> null
    }

    /**
     * Maps child UI state to parent
     * @param child Child UI state
     */
    override fun mapUiState(child: RegisterUiState): WelcomeUiState = WelcomeUiState.Register(child)

    /**
     * Returns user to email entry screen
     */
    override fun backToEmailEntry() {
        Napier.d("Transferring to e-mail entry...")
        setMachineState(context.factory.emailEntry(data))
    }

    /**
     * Authentication complete
     */
    override fun complete() {
        Napier.d("Transferring to complete screen...")
        setMachineState(context.factory.complete(email))
    }

    @Factory
    class StateFactory : KoinComponent {
        operator fun invoke(
            context: WelcomeContext,
            data: WelcomeDataState
        ): CommonMachineState<WelcomeGesture, WelcomeUiState> = RegistrationFlowState(
            context = context,
            data = data,
            startRegister = { host, email -> get<RegisterFlowStarter> { parametersOf(host) }.start(email) }
        )
    }
}

private typealias RegistrationProxy = ProxyMachineState<WelcomeGesture, WelcomeUiState, RegisterGesture, RegisterUiState>
