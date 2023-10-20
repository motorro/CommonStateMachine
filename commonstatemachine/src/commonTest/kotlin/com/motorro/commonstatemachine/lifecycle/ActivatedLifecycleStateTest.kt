package com.motorro.commonstatemachine.lifecycle

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ActivatedLifecycleStateTest {
    private lateinit var ls: ActivatedLifecycleState

    @BeforeTest
    fun init() {
        ls = ActivatedLifecycleState(LifecycleState.State.PAUSED)
    }

    @Test
    fun returnsInitialState() {
        assertEquals(LifecycleState.State.PAUSED, ls.getState())
    }

    @Test
    fun activates() {
        var activated = false
        ls.addObserver {
            assertEquals(LifecycleState.State.ACTIVE, it)
            activated = true
        }
        ls.activate()
        assertTrue(activated)
        assertEquals(LifecycleState.State.ACTIVE, ls.getState())
    }

    @Test
    fun deactivates() {
        ls.activate()
        assertEquals(LifecycleState.State.ACTIVE, ls.getState())

        var deactivated = false
        ls.addObserver {
            assertEquals(LifecycleState.State.PAUSED, it)
            deactivated = true
        }
        ls.deactivate()
        assertTrue(deactivated)
        assertEquals(LifecycleState.State.PAUSED, ls.getState())
    }
}