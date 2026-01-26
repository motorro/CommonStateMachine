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

import com.motorro.statemachine.commoncore.log.Logger
import com.motorro.statemachine.di.api.SessionManager
import com.motorro.statemachine.di.api.data.Session
import com.motorro.statemachine.di.app.data.MainGesture
import com.motorro.statemachine.di.app.data.MainUiState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

internal class Content(context: MainContext, private val sessionManager: SessionManager) : BaseMainState(context) {
    override fun doStart() {
        subscribeSession()
    }

    private fun subscribeSession() = sessionManager
        .session
        .onEach {
            when(it) {
                is Session.None -> {
                    Logger.d("No session. Switching to Auth...")
                    setMachineState(factory.auth())
                }
                is Session.Active -> render(it)
            }
        }
        .launchIn(stateScope)

    override fun doProcess(gesture: MainGesture) {
        when (gesture) {
            MainGesture.Back -> {
                Logger.d("Back. Terminating...")
                setMachineState(factory.terminated())
            }
            MainGesture.Logout -> {
                Logger.d("Logging out...")
                setMachineState(factory.loggingOut())
            }
            else -> super.doProcess(gesture)
        }
    }

    private fun render(session: Session.Active) {
        setUiState(MainUiState.Content(session))
    }

    class Factory @Inject constructor(private val sessionManager: SessionManager) {
        fun invoke(context: MainContext) = Content(
            context,
            sessionManager
        )
    }
}