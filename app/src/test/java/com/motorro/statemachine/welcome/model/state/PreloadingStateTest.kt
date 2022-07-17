@file:OptIn(ExperimentalCoroutinesApi::class)

package com.motorro.statemachine.welcome.model.state

import com.motorro.statemachine.welcome.data.WelcomeGesture
import com.motorro.statemachine.welcome.usecase.Preload
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test

class PreloadingStateTest : BaseStateTest() {
    private val string = "Welcome"
    private val preload: Preload = mockk {
        coEvery { this@mockk.invoke() } returns string
    }
    private lateinit var state: PreloadingState
    private val welcome: WelcomeState = mockk()

    init {
        every { factory.welcome(any()) } returns welcome
    }

    override fun before() {
        super.before()
        state = PreloadingState(context, preload)
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
            stateMachine.setUiState(R_CONTENT)
        }
        verify {
            renderer.renderPreloading()
        }
    }

    @Test
    fun transfersToWelcomeWhenLoaded() = runTest {
        state.start(stateMachine)
        advanceUntilIdle()

        verify { stateMachine.setMachineState(welcome) }
        verify { factory.welcome(string) }
        coVerify { preload() }
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