package com.motorro.statemachine.registration.model.state

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import org.junit.Test
import kotlin.test.assertTrue

internal class RegistrationStateTest : BaseStateTest() {
    private class ScopeTestState(context: RegistrationContext) : RegistrationState(context) {
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

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun cancelsScopeOnClear() = runTest {
        val state = ScopeTestState(context)

        state.start(stateMachine)
        yield()
        state.clear()
        yield()

        assertTrue { state.cancelled }
    }
}