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

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.statemachine.di.api.data.Session
import com.motorro.statemachine.di.app.data.MainGesture
import com.motorro.statemachine.di.app.data.MainUiState
import javax.inject.Inject
import javax.inject.Provider

/**
 * Main state factory
 */
internal interface MainFactory {
    fun auth(): CommonMachineState<MainGesture, MainUiState>
    fun loggingIn(session: Session.Active): CommonMachineState<MainGesture, MainUiState>
    fun loggingOut(): CommonMachineState<MainGesture, MainUiState>
    fun content(): CommonMachineState<MainGesture, MainUiState>
    fun terminated(): CommonMachineState<MainGesture, MainUiState>

    class Impl @Inject constructor(
        private val createContent: Provider<Content.Factory>,
        private val createAuth: Provider<AuthProxy.Factory>,
        private val createUpdateSession: Provider<UpdatingSession.Factory>,
    ) : MainFactory {

        private val context = object : MainContext {
            override val factory: MainFactory get() = this@Impl
        }

        override fun auth() = createAuth.get().invoke(
            context
        )

        override fun loggingIn(session: Session.Active) = createUpdateSession.get().invoke(
            context,
            session
        )

        override fun loggingOut() = createUpdateSession.get().invoke(
            context,
            Session.None
        )

        override fun content() = createContent.get().invoke(
            context
        )

        override fun terminated() = object : CommonMachineState<MainGesture, MainUiState>() {
            override fun doStart() {
                setUiState(MainUiState.Terminated)
            }
        }
    }
}
