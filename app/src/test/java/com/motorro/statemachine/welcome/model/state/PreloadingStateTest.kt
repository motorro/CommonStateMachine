@file:OptIn(ExperimentalCoroutinesApi::class)

package com.motorro.statemachine.welcome.model.state

import com.motorro.statemachine.commonapi.coroutines.TestDispatchers
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

class PreloadingStateTest : BaseStateTest() {
    private val state = PreloadingState(context, resourceWrapper, TestDispatchers)
    private val welcome: WelcomeState = mockk()

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
            stateMachine.setUiState(WelcomeUiState.Loading)
        }
    }

    @Test
    fun transfersToWelcomeWhenLoaded() = runTest {
        state.start(stateMachine)
        advanceUntilIdle()

        verify { stateMachine.setMachineState(welcome) }
        verify { factory.welcome(R_STRING) }
    }

    @Test
    fun terminatesOnBack() = runTest {
        val terminated: WelcomeState = mockk()
        every { factory.terminate() } returns terminated

        state.start(stateMachine)
        state.process(WelcomeGesture.Back)

        verify { stateMachine.setMachineState(terminated) }
        verify { factory.terminate() }
    }
}