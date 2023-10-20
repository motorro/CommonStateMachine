package com.motorro.commonstatemachine.lifecycle

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BaseLifecycleStateTest {
    private lateinit var ls: BaseMachineLifecycle

    @BeforeTest
    fun init() {
        ls = BaseMachineLifecycle(MachineLifecycle.State.PAUSED)
    }

    @Test
    fun returnsInitialState() {
        assertEquals(MachineLifecycle.State.PAUSED, ls.getState())
    }

    @Test
    fun returnsSubscribersStatus() {
        val observer = MachineLifecycle.Observer {
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
            assertEquals(MachineLifecycle.State.ACTIVE, it)
            updated = true
        }

        ls.setState(MachineLifecycle.State.ACTIVE)

        assertEquals(MachineLifecycle.State.ACTIVE, ls.getState())
        assertTrue(updated)
    }
}