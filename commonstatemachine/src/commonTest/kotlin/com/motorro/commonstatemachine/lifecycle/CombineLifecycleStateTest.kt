package com.motorro.commonstatemachine.lifecycle

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CombineLifecycleStateTest {

    private lateinit var parent: ActivatedLifecycleState
    private lateinit var child: ActivatedLifecycleState
    private lateinit var ls: LifecycleState

    private lateinit var s1: MutableList<LifecycleState.State>
    private lateinit var s2: MutableList<LifecycleState.State>
    private fun mix(s1: LifecycleState.State, s2: LifecycleState.State): LifecycleState.State {
        this.s1.add(s1)
        this.s2.add(s2)

        return if (LifecycleState.State.ACTIVE == s1 || LifecycleState.State.ACTIVE == s2) {
            LifecycleState.State.ACTIVE
        } else {
            LifecycleState.State.PAUSED
        }
    }

    @BeforeTest
    fun init() {
        s1 = mutableListOf()
        s2 = mutableListOf()

        parent = ActivatedLifecycleState(LifecycleState.State.PAUSED)
        child = ActivatedLifecycleState(LifecycleState.State.PAUSED)

        ls = CombineLifecycleState(parent, child, ::mix)
    }

    @Test
    fun returnsCombinedState() {
        assertEquals(LifecycleState.State.PAUSED, ls.getState())
        assertEquals(listOf(LifecycleState.State.PAUSED), s1)
        assertEquals(listOf(LifecycleState.State.PAUSED), s2)
    }

    @Test
    fun callsUpstreamOnEveryStateCheck() {
        assertEquals(LifecycleState.State.PAUSED, ls.getState())
        assertEquals(LifecycleState.State.PAUSED, ls.getState())
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
        val observer = LifecycleState.Observer {
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

        assertEquals(LifecycleState.State.ACTIVE, s1.last())
        assertEquals(LifecycleState.State.PAUSED, s2.last())
    }

    @Test
    fun updatesWhenChildChanges() {
        var updated = false
        ls.addObserver {
            updated = true
        }

        child.activate()

        assertTrue(updated)

        assertEquals(LifecycleState.State.PAUSED, s1.last())
        assertEquals(LifecycleState.State.ACTIVE, s2.last())
    }
}