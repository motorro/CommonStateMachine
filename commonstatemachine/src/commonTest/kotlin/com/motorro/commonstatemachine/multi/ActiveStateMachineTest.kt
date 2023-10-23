/*
 * Copyright 2023 Nikolai Kotchetkov.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.motorro.commonstatemachine.multi

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.StateMock
import com.motorro.commonstatemachine.lifecycle.MachineLifecycle
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ActiveStateMachineTest {
    private val intKey = object : MachineKey<Int, String>(null) { }
    private lateinit var state: StateMock<Int, String>
    private lateinit var machine: ActiveStateMachine<Int, String>

    private lateinit var status: MutableList<MachineLifecycle.State>
    private lateinit var uiState: MutableList<Any>

    @BeforeTest
    fun init() {
        status = mutableListOf()
        uiState = mutableListOf()

        state = StateMock()
        val init = object : MachineInit<Int, String> {
            override val key: MachineKey<Int, String> = intKey
            override val initialUiState: String = "Initial"
            override val init: (MachineLifecycle) -> CommonMachineState<Int, String> = { ls ->
                ls.addObserver {
                    status.add(it)
                }
                state
            }
        }
        machine = ActiveStateMachine(init) { _, u -> uiState.add(u) }
    }

    @Test
    fun hasInitialUiStateWhenNotStartedAndInactive() {
        assertEquals("Initial", machine.getUiState())
    }

    @Test
    fun startsMachineWhenFirstActivated() {
        machine.activate()
        assertTrue { machine.isActive() }
        assertTrue { state.started }
    }

    @Test
    fun updatesUiWhenProxyUpdates() {
        machine.activate()
        state.doSetUiState("New")
        assertEquals("New", machine.getUiState())
        assertEquals(listOf<Any>("New"), uiState)
    }

    @Test
    fun updatesStatesWithGestures() {
        machine.activate()
        machine.process(1)
        assertEquals(listOf(1), state.processed)
    }

    @Test
    fun updatesLifecycleOnDeactivate() {
        machine.activate()
        machine.deactivate()
        assertEquals(listOf(MachineLifecycle.State.PAUSED), status)
    }

    @Test
    fun updatesLifecycleOnReactivate() {
        machine.activate()
        machine.deactivate()
        machine.activate()
        assertEquals(listOf(MachineLifecycle.State.PAUSED, MachineLifecycle.State.ACTIVE), status)
    }

    @Test
    fun clearsMachineOnClear() {
        machine.activate()
        machine.clear()
        assertTrue { state.cleared }
    }

    @Test
    fun disposesMachineOnDispose() {
        machine.activate()
        machine.dispose()
        assertFalse { machine.isActive() }
        assertTrue { state.cleared }
        assertEquals(MachineLifecycle.State.PAUSED, status.last())
    }
}