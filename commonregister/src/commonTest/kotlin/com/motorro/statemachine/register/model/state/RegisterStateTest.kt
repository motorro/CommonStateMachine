@file:Suppress("RemoveExplicitTypeArguments")

package com.motorro.statemachine.register.model.state

import com.motorro.statemachine.register.MockRegistration
import com.motorro.statemachine.register.R_CONTENT
import com.motorro.statemachine.register.data.RegisterDataState
import com.motorro.statemachine.register.data.RegisterGesture
import com.motorro.statemachine.register.data.RegisterUiState
import com.motorro.statemachine.welcome.data.GOOD
import com.motorro.statemachine.welcome.data.WelcomeDataState
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class RegisterStateTest: BaseStateTest() {
    private val password = "password"
    private val data = RegisterDataState(WelcomeDataState(GOOD), password)

    private val registration = MockRegistration()
    private lateinit var state: RegistrationState

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
            listOf(data.commonData.email),
            host.completes
        )
    }

    @Test
    fun returnsToEmailEntryOnBack() = runTest {
        state.start(stateMachine)

        state.process(RegisterGesture.Back)

        assertEquals(
            listOf(data.commonData),
            host.backToEmails
        )
    }
}