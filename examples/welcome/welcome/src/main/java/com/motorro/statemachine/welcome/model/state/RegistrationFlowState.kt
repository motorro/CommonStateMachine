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

package com.motorro.statemachine.welcome.model.state

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.ProxyMachineState
import com.motorro.statemachine.commonapi.welcome.model.state.WelcomeFeatureHost
import com.motorro.statemachine.register.data.RegisterGesture
import com.motorro.statemachine.register.data.RegisterUiState
import com.motorro.statemachine.register.di.RegisterComponentBuilder
import com.motorro.statemachine.register.di.RegisterEntryPoint
import com.motorro.statemachine.welcome.data.WelcomeDataState
import com.motorro.statemachine.welcome.data.WelcomeGesture
import com.motorro.statemachine.welcome.data.WelcomeUiState
import dagger.hilt.EntryPoints
import dagger.hilt.android.scopes.ViewModelScoped
import timber.log.Timber
import javax.inject.Inject

/**
 * Proxy for registration flow
 * Adapts login flow sub-graph to registration flow
 */
class RegistrationFlowState(
    private val context: WelcomeContext,
    private val data: WelcomeDataState,
    private val registerComponentBuilder: RegisterComponentBuilder
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
    override fun init(): CommonMachineState<RegisterGesture, RegisterUiState> {
        val component = registerComponentBuilder.host(this).build()
        val starter = EntryPoints.get(component, RegisterEntryPoint::class.java).flowStarter()

        return starter.start(email)
    }

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
        Timber.d("Transferring to e-mail entry...")
        setMachineState(context.factory.emailEntry(data))
    }

    /**
     * Authentication complete
     */
    override fun complete() {
        Timber.d("Transferring to complete screen...")
        setMachineState(context.factory.complete(email))
    }

    @ViewModelScoped
    class Factory @Inject constructor(private val registerComponentBuilder: RegisterComponentBuilder) {
        operator fun invoke(
            context: WelcomeContext,
            data: WelcomeDataState
        ): CommonMachineState<WelcomeGesture, WelcomeUiState> = RegistrationFlowState(
            context,
            data,
            registerComponentBuilder
        )
    }
}

private typealias RegistrationProxy = ProxyMachineState<WelcomeGesture, WelcomeUiState, RegisterGesture, RegisterUiState>
