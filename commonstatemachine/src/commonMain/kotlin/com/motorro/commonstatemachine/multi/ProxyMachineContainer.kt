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

    companion object {
        /**
         * Creates a container where all machines run together and their [LifecycleState] is
         * always [LifecycleState.State.ACTIVE]
         * @param init Machine init
         */
        fun allTogether(init: Collection<MachineInit<*, *>>): ProxyMachineContainer = AllTogetherMachineContainer(init)
    }
}

/**
 * Container that activates machine
 */
interface ActiveMachineContainer : ProxyMachineContainer {
    /**
     * Retrieves currently active machine key
     */
    fun getActive(): Set<MachineKey<out Any, out Any>>

    /**
     * Sets active machine given the keys
     */
    fun setActive(keys: Set<MachineKey<out Any, out Any>>)

    /**
     * Sets active machine given the key
     */
    fun setActive(vararg key: MachineKey<out Any, out Any>) = setActive(key.toSet())

    companion object {
        /**
         * Creates a container where machines may be activated and deactivated
         * Check their [LifecycleState] passed to init to guess if machine is active or paused
         * That may come handy when implementing some view-paging or nav-bar views
         * when you want to preserve machine states on several pages but want to pause
         * some resource-intensive pending operations when machine is not "in focus"
         * @param init Machine init
         * @param initiallyActive Machines that are initially active. Defaults to first machine in [init]
         */
        fun some(
            init: Collection<MachineInit<*, *>>,
            initiallyActive: Set<MachineKey<*, *>> = setOf(init.first().key)
        ): ProxyMachineContainer = SomeActiveMachineContainer(init, initiallyActive)
    }
}

/**
 * All machines run in parallel without any lifecycle management
 * @param init Machine init
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
        machines.forEach { it.value.start() }
    }

    /**
     * Returns a map of proxy machines
     */
    override fun getMachines(): MachineMap = machines

    /**
     * Clears machines
     */
    override fun clear() {
        machines.forEach { it.value.clear() }
    }
}

/**
 * Some machines run in active lifecycle state
 * The others are suspended when not active
 * @param init Machine init
 * @param initiallyActive Machines that are initially active.
 */
internal class SomeActiveMachineContainer(
    private val init: Collection<MachineInit<*, *>>,
    private val initiallyActive: Set<MachineKey<*, *>>
) : ActiveMachineContainer {

    /**
     * Proxy machines
     */
    private var machines: Map<MachineKey<out Any, out Any>, ActiveStateMachine<out Any, out Any>> = emptyMap()

    /**
     * Starts machines
     */
    override fun start(onUiChanged: (key: MachineKey<out Any, out Any>, uiState: Any) -> Unit) {
        machines = init.associate { i -> i.key to ActiveStateMachine(i, onUiChanged) }
        setActive(initiallyActive)
    }

    /**
     * Returns a map of proxy machines
     */
    override fun getMachines(): MachineMap = machines

    /**
     * Clears machines
     */
    override fun clear() {
        machines.forEach { it.value.clear() }
    }

    /**
     * Retrieves currently active machine key
     */
    override fun getActive(): Set<MachineKey<out Any, out Any>> {
        return machines.entries.filter { (_, machine) -> machine.isActive() }.map { it.key }.toSet()
    }

    /**
     * Sets active machine given the key
     */
    override fun setActive(keys: Set<MachineKey<out Any, out Any>>) {
        val active = getActive()
        val toDeactivate = active.minus(keys)
        val toActivate = keys.minus(active)

        toDeactivate.forEach {
            machines[it]?.deactivate()
        }
        toActivate.forEach {
            machines[it]?.activate()
        }
    }
}