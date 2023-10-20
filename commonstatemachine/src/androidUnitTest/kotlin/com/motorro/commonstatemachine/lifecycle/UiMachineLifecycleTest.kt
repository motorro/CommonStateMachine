package com.motorro.commonstatemachine.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.testing.TestLifecycleOwner
import com.motorro.commonstatemachine.lifecycle.UiMachineLifecycle.Companion.getObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class UiMachineLifecycleTest {
    private lateinit var ls: UiMachineLifecycle

    @Before
    fun init() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        ls = UiMachineLifecycle()
    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }

    @Test
    fun initiallyPaused() {
        assertEquals(MachineLifecycle.State.PAUSED, ls.getState())
    }

    @Test
    fun updatesLifecycleWithObserver() {
        val uls = TestLifecycleOwner(Lifecycle.State.RESUMED)

        ls.getObserver(uls.lifecycle)
        assertEquals(MachineLifecycle.State.ACTIVE, ls.getState())
    }

    @Test
    fun updatesSubscribersWhenStateChanges() {
        var updated = false
        val uls = TestLifecycleOwner(Lifecycle.State.RESUMED)

        ls.getObserver(uls.lifecycle)
        assertEquals(MachineLifecycle.State.ACTIVE, ls.getState())

        ls.addObserver {
            assertEquals(MachineLifecycle.State.PAUSED, it)
            updated = true
        }
        uls.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        assertTrue(updated)

        Dispatchers.resetMain()
    }
}