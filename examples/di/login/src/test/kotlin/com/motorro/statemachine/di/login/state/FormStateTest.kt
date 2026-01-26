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

import com.motorro.statemachine.di.login.data.LoginDataState
import com.motorro.statemachine.di.login.data.LoginGesture
import com.motorro.statemachine.di.login.data.LoginUiState
import io.mockk.Ordering
import io.mockk.coVerify
import io.mockk.every
import org.junit.Test
import kotlin.test.assertEquals

internal class FormStateTest : BaseStateTest() {

    private lateinit var state: BaseLoginState

    override fun doInit() {
        state = FormState(context, LoginDataState())
    }

    @Test
    fun rendersDataOnStart() = test {
        state.start(stateMachine)

        coVerify {
            stateMachine.setUiState(LoginUiState.Form(username = "", password = "", loginEnabled = false))
        }
    }

    @Test
    fun willNotEnableLoginWhenUserEmpty() = test {
        state.start(stateMachine)
        state.process(LoginGesture.PasswordChanged("pass"))
        state.process(LoginGesture.Action)

        coVerify(exactly = 0) {
            factory.loggingIn(any())
            stateMachine.setMachineState(any())
        }
    }

    @Test
    fun willNotEnableLoginWhenPasswordEmpty() = test {
        state.start(stateMachine)
        state.process(LoginGesture.UsernameChanged("user"))
        state.process(LoginGesture.Action)

        coVerify(exactly = 0) {
            factory.loggingIn(any())
            stateMachine.setMachineState(any())
        }
    }

    @Test
    fun updatesUsername() = test {
        state.start(stateMachine)
        state.process(LoginGesture.UsernameChanged("user"))

        coVerify(ordering = Ordering.ORDERED) {
            stateMachine.setUiState(LoginUiState.Form(username = "", password = "", loginEnabled = false))
            stateMachine.setUiState(LoginUiState.Form(username = "user", password = "", loginEnabled = false))
        }
    }

    @Test
    fun updatesPassword() = test {
        state.start(stateMachine)
        state.process(LoginGesture.PasswordChanged("pass"))

        coVerify(ordering = Ordering.ORDERED) {
            stateMachine.setUiState(LoginUiState.Form(username = "", password = "", loginEnabled = false))
            stateMachine.setUiState(LoginUiState.Form(username = "", password = "pass", loginEnabled = false))
        }
    }

    @Test
    fun enablesLoginWhenUserAndPassSet() = test {
        state.start(stateMachine)
        state.process(LoginGesture.UsernameChanged("user"))
        state.process(LoginGesture.PasswordChanged("pass"))

        coVerify(ordering = Ordering.ORDERED) {
            stateMachine.setUiState(LoginUiState.Form(username = "", password = "", loginEnabled = false))
            stateMachine.setUiState(LoginUiState.Form(username = "user", password = "", loginEnabled = false))
            stateMachine.setUiState(LoginUiState.Form(username = "user", password = "pass", loginEnabled = true))
        }
    }

    @Test
    fun startsLogin() = test {
        every { factory.loggingIn(any()) } returns nextState

        state.start(stateMachine)
        state.process(LoginGesture.UsernameChanged("user"))
        state.process(LoginGesture.PasswordChanged("pass"))
        state.process(LoginGesture.Action)

        coVerify {
            factory.loggingIn(withArg {
                assertEquals("user", it.username)
                assertEquals("pass", it.password)
            })
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun terminatesOnBack() {
        every { factory.terminated() } returns nextState

        state.start(stateMachine)
        state.process(LoginGesture.Back)

        coVerify {
            factory.terminated()
            stateMachine.setMachineState(nextState)
        }
    }
}