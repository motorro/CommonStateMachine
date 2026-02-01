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

package com.motorro.statemachine.di.login.state

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.statemachine.di.api.AuthFlowHost
import com.motorro.statemachine.di.api.AuthGesture
import com.motorro.statemachine.di.api.AuthUiState
import com.motorro.statemachine.di.api.data.Session
import com.motorro.statemachine.di.login.data.LoginDataState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Sub-flow state factory
 */
internal interface LoginFactory {
    fun form(data: LoginDataState): CommonMachineState<AuthGesture, AuthUiState>
    fun loggingIn(data: LoginDataState): CommonMachineState<AuthGesture, AuthUiState>
    fun complete(session: Session.Active): CommonMachineState<AuthGesture, AuthUiState>
    fun error(data: LoginDataState, error: Throwable): CommonMachineState<AuthGesture, AuthUiState>
    fun terminated(): CommonMachineState<AuthGesture, AuthUiState>

    @AssistedFactory
    interface Factory {
        operator fun invoke(flowHost: AuthFlowHost): Impl
    }

    class Impl @AssistedInject constructor(@Assisted flowHost: AuthFlowHost) : LoginFactory {

        private val context = object : LoginContext {
            override val factory: LoginFactory = this@Impl
            override val flowHost: AuthFlowHost = flowHost
        }

        override fun form(data: LoginDataState) = FormState(
            context, 
            data
        )

        override fun loggingIn(data: LoginDataState) = LoggingIn(
            context,
            data
        )

        override fun complete(session: Session.Active) = object : CommonMachineState<AuthGesture, AuthUiState>() {
            override fun doStart() {
                context.flowHost.onComplete(session)
            }
        }

        override fun error(data: LoginDataState, error: Throwable) = Error(
            context,
            data,
            error
        )

        override fun terminated() = object : CommonMachineState<AuthGesture, AuthUiState>() {
            override fun doStart() {
                context.flowHost.onComplete(null)
            }
        }
    }
}