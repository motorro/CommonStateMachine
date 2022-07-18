/*
 * Copyright 2022 Nikolai Kotchetkov.
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

package com.motorro.commonstatemachine

import kotlin.test.Test
import kotlin.test.assertTrue

internal class ProxyMachineStateTest {

    private val stateMachine = MachineMock<String, String>()

    private class TestProxyState(private val state: CommonMachineState<Int, Int>) : com.motorro.commonstatemachine.ProxyMachineState<String, String, Int, Int>() {
        override fun init(): CommonMachineState<Int, Int> = state
        override fun mapGesture(parent: String): Int? = parent.toInt().takeIf { it >= 0 }
        override fun mapUiState(child: Int): String = child.toString()
    }

    @Test
    fun startsInitialStateOnStart() {
        val child = object : CommonMachineState<Int, Int>() {
            var started = false

            override fun doStart() {
                started = true
            }
        }
        val state = TestProxyState(child)

        state.start(stateMachine)

        assertTrue { child.started }
    }

    @Test
    fun delegatesGestureToCurrentState() {
        val child = StateMock<Int, Int>()
        val state = TestProxyState(child)

        state.start(stateMachine)
        state.process("1")

        assertTrue { child.processed.contains(1) }
    }

    @Test
    fun ifGestureMapperReturnsNullDoesNotCallChild() {
        val child = StateMock<Int, Int>()
        val state = TestProxyState(child)

        state.start(stateMachine)
        state.process("-1")

        assertTrue { child.processed.isEmpty() }
    }

    @Test
    fun upstreamsUiChanges() {
        val child = object : CommonMachineState<Int, Int>() {
            fun update() {
                setUiState(1)
            }
        }
        val state = TestProxyState(child)

        state.start(stateMachine)
        child.update()

        assertTrue { stateMachine.uiStates.contains("1") }
    }

    @Test
    fun cleansUpChildState() {
        val child = StateMock<Int, Int>()
        val state = TestProxyState(child)

        state.start(stateMachine)
        state.clear()

        assertTrue { child.cleared }
    }
}