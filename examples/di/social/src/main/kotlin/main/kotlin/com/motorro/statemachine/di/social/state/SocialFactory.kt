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

package main.kotlin.com.motorro.statemachine.di.social.state

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.statemachine.di.api.AuthFlowHost
import com.motorro.statemachine.di.api.AuthGesture
import com.motorro.statemachine.di.api.AuthUiState
import com.motorro.statemachine.di.api.data.Session
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Sub-flow state factory
 */
internal interface SocialFactory {
    fun form(): CommonMachineState<AuthGesture, AuthUiState>
    fun loggingIn(): CommonMachineState<AuthGesture, AuthUiState>
    fun complete(session: Session.Active): CommonMachineState<AuthGesture, AuthUiState>
    fun terminated(): CommonMachineState<AuthGesture, AuthUiState>

    @AssistedFactory
    interface Factory {
        operator fun invoke(flowHost: AuthFlowHost): Impl
    }

    class Impl @AssistedInject constructor(@Assisted flowHost: AuthFlowHost) : SocialFactory {

        private val context = object : SocialContext {
            override val factory: SocialFactory = this@Impl
            override val flowHost: AuthFlowHost = flowHost
        }

        override fun form() = FormState(
            context
        )

        override fun loggingIn() = LoggingIn(
            context,
        )

        override fun complete(session: Session.Active) = object : CommonMachineState<AuthGesture, AuthUiState>() {
            override fun doStart() {
                context.flowHost.onComplete(session)
            }
        }

        override fun terminated() = object : CommonMachineState<AuthGesture, AuthUiState>() {
            override fun doStart() {
                context.flowHost.onComplete()
            }
        }
    }
}