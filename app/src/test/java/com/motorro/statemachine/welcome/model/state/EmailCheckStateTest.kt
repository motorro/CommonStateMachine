@file:OptIn(ExperimentalCoroutinesApi::class)

package com.motorro.statemachine.welcome.model.state

import com.motorro.statemachine.welcome.data.GOOD
import com.motorro.statemachine.welcome.data.WelcomeDataState
import com.motorro.statemachine.welcome.data.WelcomeGesture
import com.motorro.statemachine.welcome.usecase.CheckEmail
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test

class EmailCheckStateTest : BaseStateTest() {
    private val data = WelcomeDataState(GOOD)

    private val checkEmail: CheckEmail = mockk()
    private lateinit var state: EmailCheckState
    private val login: WelcomeState = mockk()
    private val register: WelcomeState = mockk()

    init {
        every { factory.loginFlow(any()) } returns login
        every { factory.registrationFlow(any()) } returns register
    }

    override fun before() {
        super.before()
        state = EmailCheckState(context, data, checkEmail)
    }

    @After
    override fun after() {
        super.after()
        state.clear()
    }

    @Test
    fun displaysLoadingOnStart() = runTest {
        coEvery { checkEmail(any()) } returns true
        state.start(stateMachine)

        verify {
            stateMachine.setUiState(R_CONTENT)
        }
        verify {
            renderer.renderChecking(data)
        }
    }

    @Test
    fun transfersToLoginWhenRegistered() = runTest {
        coEvery { checkEmail(any()) } returns true

        state.start(stateMachine)
        advanceUntilIdle()

        verify { stateMachine.setMachineState(login) }
        verify { factory.loginFlow(data) }
        coVerify { checkEmail(GOOD) }
    }

    @Test
    fun transfersToRegistrationWhenNotRegistered() = runTest {
        coEvery { checkEmail(any()) } returns false

        state.start(stateMachine)
        advanceUntilIdle()

        verify { stateMachine.setMachineState(register) }
        verify { factory.registrationFlow(data) }
        coVerify { checkEmail(GOOD) }
    }

    @Test
    fun movesBackToEmailEntryOnBack() = runTest {
        coEvery { checkEmail(any()) } returns true

        val emailEntry: WelcomeState = mockk()
        every { factory.emailEntry(any()) } returns emailEntry

        state.start(stateMachine)
        state.process(WelcomeGesture.Back)

        verify { stateMachine.setMachineState(emailEntry) }
        verify { factory.emailEntry(data) }
    }
}