@file:OptIn(ExperimentalCoroutinesApi::class)

package com.motorro.statemachine.login.model.state

import com.motorro.statemachine.coroutines.TestDispatchers
import com.motorro.statemachine.login.data.LoginDataState
import com.motorro.statemachine.login.data.LoginGesture
import com.motorro.statemachine.login.data.LoginUiState
import com.motorro.statemachine.commonapi.data.BAD
import com.motorro.statemachine.commonapi.data.GOOD
import com.motorro.statemachine.commonapi.data.RegistrationDataState
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test
import kotlin.test.assertTrue

internal class CredentialsCheckStateTest : BaseStateTest() {
    private val passwordEntry: LoginState = mockk()
    private val error: LoginState = mockk()

    init {
        every { factory.passwordEntry(any()) } returns passwordEntry
        every { factory.error(any(), any()) } returns error
        every { host.complete(any()) } just Runs
    }

    private fun createState(data: LoginDataState) = CredentialsCheckState(
        context,
        data,
        TestDispatchers
    )

    private lateinit var state: CredentialsCheckState

    @After
    override fun after() {
        super.after()
        state.clear()
    }

    @Test
    fun displaysLoadingOnStart() = runTest {
        state = createState(LoginDataState(RegistrationDataState(GOOD), "password"))
        state.start(stateMachine)

        verify {
            stateMachine.setUiState(LoginUiState.Loading)
        }
    }

    @Test
    fun transfersToCompleteForGoodUser() = runTest {
        val data = LoginDataState(RegistrationDataState(GOOD), "password")
        state = createState(data)
        state.start(stateMachine)
        advanceUntilIdle()

        verify { host.complete(GOOD) }
    }

    @Test
    fun transfersToErrorForBadUser() = runTest {
        val data = LoginDataState(RegistrationDataState(BAD), "password")
        state = createState(data)
        state.start(stateMachine)
        advanceUntilIdle()

        verify { stateMachine.setMachineState(error) }
        verify { factory.error(data, withArg { assertTrue { it is IllegalArgumentException } }) }
    }

    @Test
    fun returnsToPasswordEntryOnBack() = runTest {
        val data = LoginDataState(RegistrationDataState(GOOD), "password")
        state = createState(data)

        state.start(stateMachine)
        state.process(LoginGesture.Back)

        verify { stateMachine.setMachineState(passwordEntry) }
        verify { factory.passwordEntry(data) }
    }
}