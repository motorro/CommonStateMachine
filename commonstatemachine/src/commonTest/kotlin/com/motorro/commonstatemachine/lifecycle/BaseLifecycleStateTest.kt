package com.motorro.commonstatemachine.lifecycle

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BaseLifecycleStateTest {
    private lateinit var ls: BaseLifecycleState

    @BeforeTest
    fun init() {
        ls = BaseLifecycleState(LifecycleState.State.PAUSED)
    }

    @Test
    fun returnsInitialState() {
        assertEquals(LifecycleState.State.PAUSED, ls.getState())
    }

    @Test
    fun returnsSubscribersStatus() {
        val observer = LifecycleState.Observer {
            //no-op
        }

        assertFalse(ls.hasObservers())

        ls.addObserver(observer)
        assertTrue(ls.hasObservers())

        ls.removeObserver(observer)
        assertFalse(ls.hasObservers())
    }

    @Test
    fun updatesState() {
        var updated = false
        ls.addObserver {
            assertEquals(LifecycleState.State.ACTIVE, it)
            updated = true
        }

        ls.setState(LifecycleState.State.ACTIVE)

        assertEquals(LifecycleState.State.ACTIVE, ls.getState())
        assertTrue(updated)
    }
}