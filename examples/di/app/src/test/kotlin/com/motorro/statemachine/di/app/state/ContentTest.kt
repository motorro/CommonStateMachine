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

import com.motorro.statemachine.di.api.SessionManager
import com.motorro.statemachine.di.api.data.Session
import com.motorro.statemachine.di.app.ACTIVE_SESSION
import com.motorro.statemachine.di.app.data.MainGesture
import com.motorro.statemachine.di.app.data.MainUiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Test

internal class ContentTest : BaseStateTest() {
    private lateinit var session: MutableStateFlow<Session>
    private lateinit var sessionManager: SessionManager

    private lateinit var state: BaseMainState

    private fun createState(active: Boolean) {
        session = MutableStateFlow(if (active) ACTIVE_SESSION else Session.None)
        sessionManager = mockk<SessionManager> {
            every { this@mockk.session } returns this@ContentTest.session
            coEvery { this@mockk.update(any()) } just runs
        }
        state = Content(context, sessionManager)
    }

    @Test
    fun displaysActiveSession() = test {
        createState(active = true)

        state.start(stateMachine)

        coVerify {
            stateMachine.setUiState(MainUiState.Content(ACTIVE_SESSION))
        }
    }

    @Test
    fun switchesToAuthWhenNotAuthorized() = test {
        createState(active = false)
        every { factory.auth() } returns nextState

        state.start(stateMachine)
        session.emit(Session.None)

        coVerify {
            factory.auth()
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun switchesToAuthWhenLoggedOut() = test {
        createState(active = true)
        every { factory.auth() } returns nextState

        state.start(stateMachine)
        session.emit(Session.None)

        coVerify {
            factory.auth()
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun terminatesOnBack() = test {
        createState(active = true)
        every { factory.terminated() } returns nextState

        state.start(stateMachine)
        state.process(MainGesture.Back)

        coVerify {
            factory.terminated()
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun transfersToLogout() = test {
        createState(active = true)
        every { factory.loggingOut() } returns nextState

        state.start(stateMachine)
        state.process(MainGesture.Logout)

        coVerify {
            factory.loggingOut()
            stateMachine.setMachineState(nextState)
        }
    }
}