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

import com.motorro.commonstatemachine.ProxyStateMachine
import com.motorro.commonstatemachine.lifecycle.LifecycleState

/**
 * Holds proxy-machines
 */
interface ProxyMachineContainer {
    /**
     * Starts machines
     */
    fun start(onUiChanged: (key: MachineKey<out Any, out Any>, uiState: Any) -> Unit)

    /**
     * Returns a map of proxy machines
     */
    fun getMachines(): MachineMap

    /**
     * Clears machines
     */
    fun clear()
}

/**
 * All machines run in parallel without any management
 */
internal class AllTogetherMachineContainer(private val init: Collection<MachineInit<*, *>>) : ProxyMachineContainer {

    /**
     * Proxy machines
     */
    private var machines: Map<MachineKey<out Any, out Any>, ProxyStateMachine<out Any, out Any>> = emptyMap()

    /**
     * Machine lifecycle that is always started
     */
    private val lifecycle = object : LifecycleState {
        override fun getState() = LifecycleState.State.ACTIVE
        override fun addObserver(observer: LifecycleState.Observer) = Unit
        override fun removeObserver(observer: LifecycleState.Observer) = Unit
    }

    /**
     * Starts machines
     */
    override fun start(onUiChanged: (key: MachineKey<out Any, out Any>, uiState: Any) -> Unit) {
        machines = init.associate { i -> i.key to i.machine(lifecycle, onUiChanged) }
        machines.forEach { it.value.doStart() }
    }

    /**
     * Returns a map of proxy machines
     */
    override fun getMachines(): MachineMap = machines

    /**
     * Clears machines
     */
    override fun clear() {
        machines.forEach { it.value.doClear() }
    }
}