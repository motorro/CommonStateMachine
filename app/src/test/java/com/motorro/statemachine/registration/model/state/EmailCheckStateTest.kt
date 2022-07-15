@file:OptIn(ExperimentalCoroutinesApi::class)

package com.motorro.statemachine.registration.model.state

import com.motorro.statemachine.coroutines.TestDispatchers
import com.motorro.statemachine.registration.data.RegistrationGesture
import com.motorro.statemachine.registration.data.RegistrationUiState
import com.motorro.statemachine.commonapi.data.GOOD
import com.motorro.statemachine.commonapi.data.RegistrationDataState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test

class EmailCheckStateTest : BaseStateTest() {
    private val login: RegistrationState = mockk()
    private val register: RegistrationState = mockk()

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
            stateMachine.setUiState(RegistrationUiState.Loading)
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
        val emailEntry: RegistrationState = mockk()
        every { factory.emailEntry(any()) } returns emailEntry

        state.start(stateMachine)
        state.process(RegistrationGesture.Back)

        verify { stateMachine.setMachineState(emailEntry) }
        verify { factory.emailEntry(data) }
    }
}