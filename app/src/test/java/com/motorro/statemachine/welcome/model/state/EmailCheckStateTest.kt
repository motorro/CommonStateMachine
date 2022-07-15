@file:OptIn(ExperimentalCoroutinesApi::class)

package com.motorro.statemachine.welcome.model.state

import com.motorro.statemachine.commonapi.data.GOOD
import com.motorro.statemachine.commonapi.data.RegistrationDataState
import com.motorro.statemachine.coroutines.TestDispatchers
import com.motorro.statemachine.welcome.data.WelcomeGesture
import com.motorro.statemachine.welcome.data.WelcomeUiState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test

class EmailCheckStateTest : BaseStateTest() {
    private val login: WelcomeState = mockk()
    private val register: WelcomeState = mockk()

    init {
        every { factory.loginFlow(any()) } returns login
        every { factory.registrationFlow(any()) } returns register
    }

    private fun createState(data: RegistrationDataState) = EmailCheckState(
        context,
        data,
        TestDispatchers
    )

    private lateinit var state: EmailCheckState

    @After
    override fun after() {
        super.after()
        state.clear()
    }

    @Test
    fun displaysLoadingOnStart() = runTest {
        state = createState(RegistrationDataState(GOOD))
        state.start(stateMachine)

        verify {
            stateMachine.setUiState(WelcomeUiState.Loading)
        }
    }

    @Test
    fun transfersToLoginWhenRegistered() = runTest {
        val data = RegistrationDataState(GOOD)
        state = createState(data)

        state.start(stateMachine)
        advanceUntilIdle()

        verify { stateMachine.setMachineState(login) }
        verify { factory.loginFlow(data) }
    }

    @Test
    fun transfersToRegistrationWhenNotRegistered() = runTest {
        val data = RegistrationDataState("someone@example.com")
        state = createState(data)

        state.start(stateMachine)
        advanceUntilIdle()

        verify { stateMachine.setMachineState(register) }
        verify { factory.registrationFlow(data) }
    }

    @Test
    fun movesBackToEmailEntryOnBack() = runTest {
        val data = RegistrationDataState(GOOD)
        state = createState(data)
        val emailEntry: WelcomeState = mockk()
        every { factory.emailEntry(any()) } returns emailEntry

        state.start(stateMachine)
        state.process(WelcomeGesture.Back)

        verify { stateMachine.setMachineState(emailEntry) }
        verify { factory.emailEntry(data) }
    }
}