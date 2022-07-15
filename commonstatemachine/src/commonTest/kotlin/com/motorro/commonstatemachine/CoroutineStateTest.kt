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
        lateinit var job: Job

        val cancelled: Boolean
            get() = job.isCancelled

        override fun doStart() {
            job = stateScope.launch {
                while(true) yield()
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