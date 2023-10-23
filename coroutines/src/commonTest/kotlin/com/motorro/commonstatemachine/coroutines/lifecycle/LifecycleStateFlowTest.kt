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

package com.motorro.commonstatemachine.coroutines.lifecycle

import com.motorro.commonstatemachine.lifecycle.BaseMachineLifecycle
import com.motorro.commonstatemachine.lifecycle.MachineLifecycle
import com.motorro.commonstatemachine.lifecycle.MachineLifecycle.State
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class LifecycleStateFlowTest {

    private lateinit var producer: BaseMachineLifecycle

    @BeforeTest
    fun init() {
        producer = BaseMachineLifecycle(State.PAUSED)
    }

    @Test
    fun convertsStateToFlow() = runTest {
        val values = mutableListOf<State>()
        val job = launch {
            producer.asFlow(this).collect { values.add(it)  }
        }

        producer.setState(State.ACTIVE)
        this.advanceUntilIdle()
        producer.setState(State.PAUSED)
        this.advanceUntilIdle()

        job.cancel()

        producer.setState(State.ACTIVE)

        assertEquals(
            listOf(State.ACTIVE, State.PAUSED),
            values
        )
    }

    @Test
    fun convertsFlowToCallback() = runTest {
        val values = mutableListOf<State>()
        val flow = MutableStateFlow(State.PAUSED)
        val observer = MachineLifecycle.Observer { state -> values.add(state) }
        val callbacks = flow.asCallback(this)

        callbacks.addObserver(observer)

        flow.value = State.ACTIVE
        this.advanceUntilIdle()
        flow.value = State.PAUSED
        this.advanceUntilIdle()

        callbacks.removeObserver(observer)

        flow.value = State.ACTIVE

        assertEquals(
            listOf(State.ACTIVE, State.PAUSED),
            values
        )
    }
}