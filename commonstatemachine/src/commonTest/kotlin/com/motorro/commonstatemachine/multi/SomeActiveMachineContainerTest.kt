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

class SomeActiveMachineContainerTest {

    private lateinit var intState: StateMock<Int, Int>
    private lateinit var stringState: StateMock<String, String>
    private lateinit var container: SomeActiveMachineContainer<Any, Any>

    private val intKey = object : MachineKey<Int, Int>() { }
    private val stringKey = object : MachineKey<String, String>() { }
    private lateinit var init1: MachineInit<Int, Int>
    private lateinit var init2: MachineInit<String, String>

    private lateinit var intLs: MutableList<MachineLifecycle.State>
    private lateinit var stringLs: MutableList<MachineLifecycle.State>

    @BeforeTest
    fun before() {
        intLs = mutableListOf()
        stringLs = mutableListOf()

        intState = StateMock()
        init1 = object : MachineInit<Int, Int> {
            override val key: MachineKey<Int, Int> = intKey
            override val initialUiState: Int = 1
            override val init: (MachineLifecycle) -> CommonMachineState<Int, Int> = { ls ->
                ls.addObserver {
                    intLs.add(it)
                }
                intState
            }
        }
        stringState = StateMock()
        init2 = object : MachineInit<String, String> {
            override val key: MachineKey<String, String> = stringKey
            override val initialUiState: String = "1"
            override val init: (MachineLifecycle) -> CommonMachineState<String, String> = { ls ->
                ls.addObserver {
                    stringLs.add(it)
                }
                stringState
            }
        }
        container = SomeActiveMachineContainer(
            listOf(init1, init2),
            setOf(intKey)
        )
    }

    @Test
    fun activatesSomeMachinesOnStart() {
        container.start { _, _ ->  }
        assertTrue { intState.started }
        assertEquals(setOf(intKey), container.getActive())
    }

    @Test
    fun clearsStartedMachinesOnClear() {
        container.start { _, _ ->  }
        container.clear()
        assertTrue { intState.started }
        assertTrue { intState.cleared }
        assertFalse { stringState.cleared }
        assertFalse { stringState.cleared }
    }

    @Test
    fun updatesParentWhenChildUpdatesUiState() {
        var updateKey: MachineKey<*, *>? = null
        var updateUiState: Any? = null
        val container = AllTogetherMachineContainer(listOf(init1))
        container.start { key, ui ->
            updateKey = key
            updateUiState = ui
        }
        intState.doSetUiState(2)
        assertEquals(intKey, updateKey)
        assertEquals(2, updateUiState)
    }

    @Test
    fun activatesAllMachines() {
        container.start { _, _ ->  }
        container.setActive(intKey, stringKey)
        assertEquals(setOf(intKey, stringKey), container.getActive())
        assertTrue { intState.started }
        assertTrue { stringState.started }
    }

    @Test
    fun deactivatesAllActiveMachines() {
        container.start { _, _ ->  }
        container.setActive(intKey, stringKey)
        container.setActive(emptySet())

        assertEquals(emptySet(), container.getActive())
        assertEquals(listOf(MachineLifecycle.State.PAUSED), intLs)
        assertEquals(listOf(MachineLifecycle.State.PAUSED), stringLs)
    }

    @Test
    fun switchesActiveMachines() {
        container.start { _, _ ->  }
        container.setActive(stringKey)

        assertEquals(setOf(stringKey), container.getActive())
        assertTrue { stringState.started }
        assertEquals(listOf(MachineLifecycle.State.PAUSED), intLs)
    }

    @Test
    fun disposesMachines() {
        container.start { _, _ ->  }
        container.setActive(intKey, stringKey)
        assertTrue { stringState.started }
        assertTrue { intState.started }
        container.dispose(intKey, stringKey)
        assertTrue { stringState.cleared }
        assertTrue { intState.cleared }
    }

    @Test
    fun disposesInactive() {
        container.start { _, _ ->  }
        container.setActive(intKey, stringKey)
        assertTrue { stringState.started }
        assertTrue { intState.started }
        container.setActive(intKey)
        assertFalse { stringState.cleared }
        container.disposeInactive()

        assertTrue { stringState.cleared }
        assertFalse { intState.cleared }
    }
}