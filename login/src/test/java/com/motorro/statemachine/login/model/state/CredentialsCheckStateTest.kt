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

@file:OptIn(ExperimentalCoroutinesApi::class)

package com.motorro.statemachine.login.model.state

import com.motorro.statemachine.commonapi.welcome.data.GOOD
import com.motorro.statemachine.commonapi.welcome.data.WelcomeDataState
import com.motorro.statemachine.login.data.LoginDataState
import com.motorro.statemachine.login.data.LoginGesture
import com.motorro.statemachine.login.usecase.CheckCredentials
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test
import kotlin.test.assertTrue

internal class CredentialsCheckStateTest : BaseStateTest() {
    private val password = "password"
    private val data = LoginDataState(WelcomeDataState(GOOD), password)

    private val checkCredentials: CheckCredentials = mockk()
    private lateinit var state: CredentialsCheckState
    private val passwordEntry: LoginState = mockk()
    private val error: LoginState = mockk()

    init {
        every { factory.passwordEntry(any()) } returns passwordEntry
        every { factory.error(any(), any()) } returns error
        every { host.complete(any()) } just Runs
    }

    override fun before() {
        super.before()
        state = CredentialsCheckState(context, data, checkCredentials)
    }

    @After
    override fun after() {
        super.after()
        state.clear()
    }

    @Test
    fun displaysLoadingOnStart() = runTest {
        coEvery { checkCredentials(any(), any()) } returns true

        state.start(stateMachine)

        verify {
            stateMachine.setUiState(R_LOADING)
        }
        verify {
            renderer.renderLoading(data)
        }
    }

    @Test
    fun transfersToCompleteForGoodUser() = runTest {
        coEvery { checkCredentials(any(), any()) } returns true

        state.start(stateMachine)
        advanceUntilIdle()

        verify { host.complete(GOOD) }
        coVerify { checkCredentials(GOOD, password) }
    }

    @Test
    fun transfersToErrorForBadUser() = runTest {
        coEvery { checkCredentials(any(), any()) } returns false

        state.start(stateMachine)
        advanceUntilIdle()

        verify { stateMachine.setMachineState(error) }
        verify { factory.error(data, withArg { assertTrue { it is IllegalArgumentException } }) }
        coVerify { checkCredentials(GOOD, password) }
    }

    @Test
    fun returnsToPasswordEntryOnBack() = runTest {
        coEvery { checkCredentials(any(), any()) } returns true

        state.start(stateMachine)
        state.process(LoginGesture.Back)

        verify { stateMachine.setMachineState(passwordEntry) }
        verify { factory.passwordEntry(data) }
    }
}