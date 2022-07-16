@file:OptIn(ExperimentalCoroutinesApi::class)

package com.motorro.statemachine.login.model.state

import com.motorro.statemachine.login.data.LoginDataState
import com.motorro.statemachine.login.data.LoginGesture
import com.motorro.statemachine.login.data.LoginUiState
import com.motorro.statemachine.login.usecase.CheckCredentials
import com.motorro.statemachine.welcome.data.GOOD
import com.motorro.statemachine.welcome.data.WelcomeDataState
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
            stateMachine.setUiState(LoginUiState.Loading)
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