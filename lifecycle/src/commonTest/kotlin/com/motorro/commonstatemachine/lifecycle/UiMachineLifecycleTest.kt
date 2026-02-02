package com.motorro.commonstatemachine.lifecycle

import androidx.lifecycle.Lifecycle
import com.motorro.commonstatemachine.lifecycle.UiMachineLifecycle.Companion.bindLifecycle
import com.motorro.commonstatemachine.test.PlatformTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class UiMachineLifecycleTest : PlatformTest() {
    private lateinit var ls: UiMachineLifecycle

    @BeforeTest
    fun init() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        ls = UiMachineLifecycle()
    }

    @AfterTest
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

        ls.bindLifecycle(uls.lifecycle)
        assertEquals(MachineLifecycle.State.ACTIVE, ls.getState())
    }

    @Test
    fun updatesSubscribersWhenStateChanges() {
        var updated = false
        val uls = TestLifecycleOwner(Lifecycle.State.RESUMED)

        ls.bindLifecycle(uls.lifecycle)
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