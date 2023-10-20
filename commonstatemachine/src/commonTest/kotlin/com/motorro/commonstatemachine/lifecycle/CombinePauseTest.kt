package com.motorro.commonstatemachine.lifecycle

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CombinePauseTest {
    private lateinit var parent: ActivatedMachineLifecycle
    private lateinit var child: ActivatedMachineLifecycle
    private lateinit var ls: MachineLifecycle


    @BeforeTest
    fun init() {
        parent = ActivatedMachineLifecycle(MachineLifecycle.State.PAUSED)
        child = ActivatedMachineLifecycle(MachineLifecycle.State.PAUSED)

        ls = parent.combinePaused(child)
    }

    @Test
    fun activatesWhenBothActive() {
        assertEquals(MachineLifecycle.State.PAUSED, ls.getState())

        child.activate()
        assertEquals(MachineLifecycle.State.PAUSED, ls.getState())

        parent.activate()
        assertEquals(MachineLifecycle.State.ACTIVE, ls.getState())
    }

    @Test
    fun deactivatesWhenParentDeactivates() {
        assertEquals(MachineLifecycle.State.PAUSED, ls.getState())

        child.activate()
        assertEquals(MachineLifecycle.State.PAUSED, ls.getState())

        parent.activate()
        assertEquals(MachineLifecycle.State.ACTIVE, ls.getState())

        parent.deactivate()
        assertEquals(MachineLifecycle.State.PAUSED, ls.getState())
    }

    @Test
    fun deactivatesWhenChildDeactivates() {
        assertEquals(MachineLifecycle.State.PAUSED, ls.getState())

        child.activate()
        assertEquals(MachineLifecycle.State.PAUSED, ls.getState())

        parent.activate()
        assertEquals(MachineLifecycle.State.ACTIVE, ls.getState())

        child.deactivate()
        assertEquals(MachineLifecycle.State.PAUSED, ls.getState())
    }
}