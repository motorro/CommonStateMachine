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

@file:Suppress("RemoveExplicitTypeArguments")

package com.motorro.commonstatemachine.examples.welcome.register.model.state

import com.motorro.commonstatemachine.examples.welcome.commonapi.data.GOOD
import com.motorro.commonstatemachine.examples.welcome.register.MockRegistration
import com.motorro.commonstatemachine.examples.welcome.register.R_CONTENT
import com.motorro.commonstatemachine.examples.welcome.register.data.RegisterDataState
import com.motorro.commonstatemachine.examples.welcome.register.data.RegisterGesture
import com.motorro.commonstatemachine.examples.welcome.register.data.RegisterUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal abstract class RegisterStateTest: BaseStateTest() {
    private val password = "password"
    private val data = RegisterDataState(GOOD, password)

    private val registration = MockRegistration()
    private lateinit var state: RegistrationState

    @BeforeTest
    override fun before() {
        super.before()
        state = RegistrationState(context, data, registration)
    }

    @AfterTest
    override fun after() {
        super.after()
        state.clear()
    }

    @Test
    fun displaysLoadingOnStart() = runTest {
        state.start(stateMachine)

        assertEquals<List<RegisterUiState>>(
            listOf(R_CONTENT),
            stateMachine.uiStates
        )
        assertEquals(
            listOf(data),
            context.renderer.registrations
        )
        assertEquals(
            listOf(GOOD to password),
            registration.invocations
        )
    }

    @Test
    fun transfersToComplete() = runTest {
        state.start(stateMachine)
        advanceUntilIdle()

        assertEquals(
            1,
            host.completes
        )
    }

    @Test
    fun returnsToEmailEntryOnBack() = runTest {
        state.start(stateMachine)

        state.process(RegisterGesture.Back)

        assertEquals(
            1,
            host.backToEmails
        )
    }
}
