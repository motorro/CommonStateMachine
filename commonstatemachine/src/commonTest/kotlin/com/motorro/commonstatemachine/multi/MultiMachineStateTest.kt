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
import com.motorro.commonstatemachine.MachineMock
import com.motorro.commonstatemachine.lifecycle.MachineLifecycle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MultiMachineStateTest {
    private data object IntKey: MachineKey<Int, Int>(null)
    private data object StringKey: MachineKey<String, String>(null)

    private class TestChildState<D: Any>(private var value: D) : CommonMachineState<D, D>() {
        override fun doStart() {
            render()
        }

        override fun doProcess(gesture: D) {
            value = gesture
            render()
        }

        private fun render() {
            setUiState(value)
        }
    }

    private open class TestState : MultiMachineState<Int, String>() {
        override val container: ProxyMachineContainer = AllTogetherMachineContainer(
            listOf(
                object : MachineInit<Int, Int> {
                    override val key: MachineKey<Int, Int> = IntKey
                    override val initialUiState: Int = 0
                    override val init: (MachineLifecycle) -> CommonMachineState<Int, Int> = {
                        TestChildState(0)
                    }
                },
                object : MachineInit<String, String> {
                    override val key: MachineKey<String, String> = StringKey
                    override val initialUiState: String = "X"
                    override val init: (MachineLifecycle) -> CommonMachineState<String, String> = {
                        TestChildState("X")
                    }
                }
            )
        )

        override fun mapGesture(parent: Int, processor: GestureProcessor) {
            processor.process(IntKey, parent)
            processor.process(StringKey, parent.toString())
        }

        override fun mapUiState(provider: UiStateProvider, changedKey: MachineKey<*, *>?): String {
            val i: Int = provider.getValue(IntKey)
            val s: String = provider.getValue(StringKey)
            return "$i - $s"
        }
    }

    @Test
    fun startsContainer() {
        val state = TestState()
        val machine = MachineMock<Int, String>("INIT")
        state.start(machine)

        assertEquals(
            listOf("INIT", "0 - X", "0 - X"),
            machine.uiStates
        )
    }

    @Test
    fun updatesMachines() {
        val state = TestState()
        val machine = MachineMock<Int, String>("INIT")
        state.start(machine)
        state.process(1)
        state.process(2)

        assertEquals(
            listOf("INIT", "0 - X", "0 - X", "1 - X", "1 - 1", "2 - 1", "2 - 2"),
            machine.uiStates
        )
    }

    @Test
    fun providesActiveMachinesKeys() {
        var tested = false
        val state = object : TestState() {
            override fun mapUiState(provider: UiStateProvider, changedKey: MachineKey<*, *>?): String {
                assertEquals(
                    setOf(IntKey, StringKey),
                    provider.getMachineKeys()
                )
                tested = true
                return super.mapUiState(provider, changedKey)
            }
        }

        val machine = MachineMock<Int, String>("INIT")
        state.start(machine)
        assertTrue { tested }
    }
}