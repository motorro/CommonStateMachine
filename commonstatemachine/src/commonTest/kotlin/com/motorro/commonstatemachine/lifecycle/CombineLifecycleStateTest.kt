package com.motorro.commonstatemachine.lifecycle

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CombineLifecycleStateTest {

    private lateinit var parent: ActivatedMachineLifecycle
    private lateinit var child: ActivatedMachineLifecycle
    private lateinit var ls: MachineLifecycle

    private lateinit var s1: MutableList<MachineLifecycle.State>
    private lateinit var s2: MutableList<MachineLifecycle.State>
    private fun mix(s1: MachineLifecycle.State, s2: MachineLifecycle.State): MachineLifecycle.State {
        this.s1.add(s1)
        this.s2.add(s2)

        return if (MachineLifecycle.State.ACTIVE == s1 || MachineLifecycle.State.ACTIVE == s2) {
            MachineLifecycle.State.ACTIVE
        } else {
            MachineLifecycle.State.PAUSED
        }
    }

    @BeforeTest
    fun init() {
        s1 = mutableListOf()
        s2 = mutableListOf()

        parent = ActivatedMachineLifecycle(MachineLifecycle.State.PAUSED)
        child = ActivatedMachineLifecycle(MachineLifecycle.State.PAUSED)

        ls = CombineMachineLifecycle(parent, child, ::mix)
    }

    @Test
    fun returnsCombinedState() {
        assertEquals(MachineLifecycle.State.PAUSED, ls.getState())
        assertEquals(listOf(MachineLifecycle.State.PAUSED), s1)
        assertEquals(listOf(MachineLifecycle.State.PAUSED), s2)
    }

    @Test
    fun callsUpstreamOnEveryStateCheck() {
        assertEquals(MachineLifecycle.State.PAUSED, ls.getState())
        assertEquals(MachineLifecycle.State.PAUSED, ls.getState())
        assertEquals(2, s1.size)
        assertEquals(2, s2.size)
    }

    @Test
    fun notSubscribedToUpstreamWhenNotSubscribed() {
        assertFalse { ls.hasObservers() }
        assertFalse { parent.hasObservers() }
        assertFalse { child.hasObservers() }
    }

    @Test
    fun subscribesToUpstreamWhenSubscribedAndUnsubscribesWhenUnsubscribed() {
        val observer = MachineLifecycle.Observer {
            // no-op
        }

        ls.addObserver(observer)
        assertTrue { ls.hasObservers() }
        assertTrue { parent.hasObservers() }
        assertTrue { child.hasObservers() }

        ls.removeObserver(observer)
        assertFalse { ls.hasObservers() }
        assertFalse { parent.hasObservers() }
        assertFalse { child.hasObservers() }
    }

    @Test
    fun updatesWhenParentChanges() {
        var updated = false
        ls.addObserver {
            updated = true
        }

        parent.activate()

        assertTrue(updated)

        assertEquals(MachineLifecycle.State.ACTIVE, s1.last())
        assertEquals(MachineLifecycle.State.PAUSED, s2.last())
    }

    @Test
    fun updatesWhenChildChanges() {
        var updated = false
        ls.addObserver {
            updated = true
        }

        child.activate()

        assertTrue(updated)

        assertEquals(MachineLifecycle.State.PAUSED, s1.last())
        assertEquals(MachineLifecycle.State.ACTIVE, s2.last())
    }
}