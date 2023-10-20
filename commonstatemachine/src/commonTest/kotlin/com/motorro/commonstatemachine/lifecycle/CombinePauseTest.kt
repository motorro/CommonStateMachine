package com.motorro.commonstatemachine.lifecycle

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CombinePauseTest {
    private lateinit var parent: ActivatedLifecycleState
    private lateinit var child: ActivatedLifecycleState
    private lateinit var ls: LifecycleState


    @BeforeTest
    fun init() {
        parent = ActivatedLifecycleState(LifecycleState.State.PAUSED)
        child = ActivatedLifecycleState(LifecycleState.State.PAUSED)

        ls = parent.combinePaused(child)
    }

    @Test
    fun activatesWhenBothActive() {
        assertEquals(LifecycleState.State.PAUSED, ls.getState())

        child.activate()
        assertEquals(LifecycleState.State.PAUSED, ls.getState())

        parent.activate()
        assertEquals(LifecycleState.State.ACTIVE, ls.getState())
    }

    @Test
    fun deactivatesWhenParentDeactivates() {
        assertEquals(LifecycleState.State.PAUSED, ls.getState())

        child.activate()
        assertEquals(LifecycleState.State.PAUSED, ls.getState())

        parent.activate()
        assertEquals(LifecycleState.State.ACTIVE, ls.getState())

        parent.deactivate()
        assertEquals(LifecycleState.State.PAUSED, ls.getState())
    }

    @Test
    fun deactivatesWhenChildDeactivates() {
        assertEquals(LifecycleState.State.PAUSED, ls.getState())

        child.activate()
        assertEquals(LifecycleState.State.PAUSED, ls.getState())

        parent.activate()
        assertEquals(LifecycleState.State.ACTIVE, ls.getState())

        child.deactivate()
        assertEquals(LifecycleState.State.PAUSED, ls.getState())
    }
}