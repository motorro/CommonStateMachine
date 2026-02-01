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

package com.motorro.statemachine.di.app.state

import com.motorro.commonstatemachine.ProxyMachineState
import com.motorro.statemachine.commoncore.log.Logger
import com.motorro.statemachine.di.api.AuthDataApi
import com.motorro.statemachine.di.api.AuthFlowHost
import com.motorro.statemachine.di.api.AuthGesture
import com.motorro.statemachine.di.api.AuthUiState
import com.motorro.statemachine.di.app.data.MainGesture
import com.motorro.statemachine.di.app.data.MainUiState
import javax.inject.Inject

internal class AuthProxy(
    private val context: MainContext,
    private val api: AuthDataApi
) : ProxyMachineState<MainGesture, MainUiState, AuthGesture, AuthUiState>(api.getDefaultUiState()) {

    private val flowHost = AuthFlowHost { result ->
        when(result) {
            null -> {
                Logger.d("Auth cancelled. Terminating...")
                setMachineState(context.factory.terminated())
            }

            else -> {
                Logger.d("Auth completed. Logging in...")
                setMachineState(context.factory.loggingIn(result))
            }
        }
    }

    override fun init() = api.init(flowHost, Unit)

    override fun mapGesture(parent: MainGesture): AuthGesture? = when (parent) {
        is MainGesture.Auth -> parent.child
        MainGesture.Back -> api.getBackGesture()
        else -> null
    }

    override fun mapUiState(child: AuthUiState): MainUiState = MainUiState.Auth(child)

    class Factory @Inject constructor(private val api: AuthDataApi) {
        fun invoke(context: MainContext) = AuthProxy(
            context,
            api
        )
    }
}