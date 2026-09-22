/*
 * Copyright 2026 Nikolai Kotchetkov.
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

package com.motorro.statemachine.skills.app.state

import com.motorro.commonstatemachine.ProxyMachineState
import com.motorro.commonstatemachine.flow.data.CommonFlowHost
import com.motorro.statemachine.skills.app.data.MainGesture
import com.motorro.statemachine.skills.app.data.MainUiState
import com.motorro.statemachine.skills.auth.api.AuthDataApi
import com.motorro.statemachine.skills.auth.api.AuthGesture
import com.motorro.statemachine.skills.auth.api.AuthInput
import com.motorro.statemachine.skills.auth.api.AuthResult
import com.motorro.statemachine.skills.auth.api.AuthUiState
import io.github.aakira.napier.Napier
import org.koin.core.annotation.Factory

internal class AuthProxyState(
    private val context: MainContext,
    private val requireAuthentication: Boolean,
    private val api: AuthDataApi
) : ProxyMachineState<MainGesture, MainUiState, AuthGesture, AuthUiState>(api.getDefaultUiState()) {

    private val flowHost = CommonFlowHost<AuthResult> { result ->
        if (result.authenticated) {
            Napier.d { "Authenticated. Checking..." }
            setMachineState(context.factory.init())
        } else {
            Napier.d { "Authentication cancelled. Terminating..." }
            setMachineState(context.factory.terminated())
        }
    }

    override fun init() = api.init(flowHost, AuthInput(requireAuthentication.not()))

    override fun mapGesture(parent: MainGesture): AuthGesture? = when (parent) {
        is MainGesture.Auth -> parent.child
        MainGesture.Back -> api.getBackGesture()
    }

    override fun mapUiState(child: AuthUiState): MainUiState = MainUiState.Auth(child)

    @Factory
    class StateFactory(private val api: AuthDataApi) {
        fun create(context: MainContext) = AuthProxyState(
            context = context,
            requireAuthentication = true,
            api = api
        )
    }
}