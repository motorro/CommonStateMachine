@file:OptIn(ExperimentalCoroutinesApi::class)

package com.motorro.statemachine.registration.model.state

import com.motorro.statemachine.coroutines.TestDispatchers
import com.motorro.statemachine.registration.data.RegistrationGesture
import com.motorro.statemachine.registration.data.RegistrationUiState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test

class PreloadingStateTest : BaseStateTest() {
    private val state = PreloadingState(context, resourceWrapper, TestDispatchers)
    private val welcome: RegistrationState = mockk()

    init {
        every { factory.welcome(any()) } returns welcome
    }

    @After
    override fun after() {
        super.after()
        state.clear()
    }

    @Test
    fun displaysLoadingOnStart() = runTest {
        state.start(stateMachine)

        verify {
            stateMachine.setUiState(RegistrationUiState.Loading)
        }
    }

    @Test
    fun transfersToWelcomeWhenLoaded() = runTest {
        state.start(stateMachine)
        advanceUntilIdle()

        verify { stateMachine.machineState = welcome }
        verify { factory.welcome(R_STRING) }
    }

    @Test
    fun terminatesOnBack() = runTest {
        val terminated: RegistrationState = mockk()
        every { factory.terminate() } returns terminated

        state.start(stateMachine)
        state.process(RegistrationGesture.Back)

        verify { stateMachine.machineState = terminated }
        verify { factory.terminate() }
    }
}