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
import com.motorro.statemachine.commonapi.welcome.data.WelcomeDataState
import com.motorro.statemachine.commonapi.welcome.model.state.WelcomeFeatureHost
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
    private val data: WelcomeDataState,
    private val loginComponentBuilder: LoginComponentBuilder
) : LoginProxy(), WelcomeFeatureHost {
    /**
     * Creates initial child state
     */
    override fun init(): CommonMachineState<LoginGesture, LoginUiState> {
        val component = loginComponentBuilder.host(this).build()
        val starter = EntryPoints.get(component, LoginEntryPoint::class.java).flowStarter()

        return starter.start(data)
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
    override fun backToEmailEntry(data: WelcomeDataState) {
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
            data: WelcomeDataState
        ): CommonMachineState<WelcomeGesture, WelcomeUiState> = LoginFlowState(
            context,
            data,
            loginComponentBuilder
        )
    }
}

private typealias LoginProxy = ProxyMachineState<WelcomeGesture, WelcomeUiState, LoginGesture, LoginUiState>
