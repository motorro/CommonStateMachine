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
import com.motorro.commonstatemachine.lifecycle.LifecycleState
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AllTogetherMachineContainerTest {

    private lateinit var state: StateMock<Int, String>

    private val intKey = object : MachineKey<Int, String>() { }
    private lateinit var init: MachineInit<Int, String>

    @BeforeTest
    fun before() {
        state = StateMock()
        init = object : MachineInit<Int, String> {
            override val key: MachineKey<Int, String> = intKey
            override val initialUiState: String = "INIT"
            override val init: (LifecycleState) -> CommonMachineState<Int, String> = {
                state
            }
        }
    }

    @Test
    fun createsMachinesOnStart() {
        val container = AllTogetherMachineContainer(listOf(init))
        container.start { _, _ ->  }
        assertTrue { state.started }
    }

    @Test
    fun clearsMachinesOnClear() {
        val container = AllTogetherMachineContainer(listOf(init))
        container.start { _, _ ->  }
        container.clear()
        assertTrue { state.cleared }
    }

    @Test
    fun updatesParentWhenChildUpdatesUiState() {
        var updateKey: MachineKey<*, *>? = null
        var updateUiState: Any? = null
        val container = AllTogetherMachineContainer(listOf(init))
        container.start { key, ui ->
            updateKey = key
            updateUiState = ui
        }
        state.doSetUiState("UPDATE")
        assertEquals(intKey, updateKey)
        assertEquals("UPDATE", updateUiState)
    }
}