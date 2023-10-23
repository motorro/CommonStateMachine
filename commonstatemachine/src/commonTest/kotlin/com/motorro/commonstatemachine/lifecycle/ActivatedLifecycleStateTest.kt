package com.motorro.commonstatemachine.lifecycle

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ActivatedLifecycleStateTest {
    private lateinit var ls: ActivatedMachineLifecycle

    @BeforeTest
    fun init() {
        ls = ActivatedMachineLifecycle(MachineLifecycle.State.PAUSED)
    }

    @Test
    fun returnsInitialState() {
        assertEquals(MachineLifecycle.State.PAUSED, ls.getState())
    }

    @Test
    fun activates() {
        var activated = false
        ls.addObserver {
            assertEquals(MachineLifecycle.State.ACTIVE, it)
            activated = true
        }
        ls.activate()
        assertTrue(activated)
        assertEquals(MachineLifecycle.State.ACTIVE, ls.getState())
    }

    @Test
    fun deactivates() {
        ls.activate()
        assertEquals(MachineLifecycle.State.ACTIVE, ls.getState())

        var deactivated = false
        ls.addObserver {
            assertEquals(MachineLifecycle.State.PAUSED, it)
            deactivated = true
        }
        ls.deactivate()
        assertTrue(deactivated)
        assertEquals(MachineLifecycle.State.PAUSED, ls.getState())
    }
}