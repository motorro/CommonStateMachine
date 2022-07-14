@file:OptIn(ExperimentalCoroutinesApi::class)

package com.motorro.commonstatemachine

import kotlinx.coroutines.*
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
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

    @BeforeTest
    fun before() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun after() {
        Dispatchers.resetMain()
    }

    @Test
    fun cancelsScopeOnClear() = runTest {
        val state = ScopeTestState()

        state.start(MachineMock())
        yield()
        state.clear()
        yield()

        assertTrue { state.cancelled }
    }
}