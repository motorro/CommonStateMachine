package com.motorro.statemachine.machine

import androidx.annotation.CallSuper
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

class CoroutineStateTest {
    private class ScopeTestState : CoroutineState<Int, Int>() {
        var cancelled = false

        override fun doStart() {
            stateScope.launch {
                try {
                    while(true) yield()
                } catch (e: CancellationException) {
                    cancelled = true
                    throw e
                }
            }
        }
    }

    @Before
    @OptIn(ExperimentalCoroutinesApi::class)
    fun before() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    @CallSuper
    @OptIn(ExperimentalCoroutinesApi::class)
    fun after() {
        Dispatchers.resetMain()
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun cancelsScopeOnClear() = runTest {
        val state = ScopeTestState()

        state.start(mockk())
        yield()
        state.clear()
        yield()

        assertTrue { state.cancelled }
    }
}