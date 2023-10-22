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
import com.motorro.commonstatemachine.lifecycle.MachineLifecycle

/**
 * Holds proxy-machines
 */
interface ProxyMachineContainer {
    /**
     * Starts machines
     */
    fun start(onUiChanged: (key: MachineKey<*, *>, uiState: Any) -> Unit)

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
         * Creates a container where all machines run together and their [MachineLifecycle] is
         * always [MachineLifecycle.State.ACTIVE]
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
    fun getActive(): Set<MachineKey<*, *>>

    /**
     * Sets active machine given the keys
     */
    fun setActive(keys: Set<MachineKey<*, *>>)

    /**
     * Sets active machine given the key
     */
    fun setActive(vararg key: MachineKey<*, *>) = setActive(key.toSet())

    /**
     * Disposes machines. All machines by [keys] are deactivated disposed and dereferenced.
     * When activating again - a new machine is created using the same init.
     * Use to cleanup memory for example on low memory alert from system
     */
    fun dispose(keys: Set<MachineKey<*, *>>)

    /**
     * Disposes machines. All machines by [key] are deactivated disposed and dereferenced.
     * When activating again - a new machine is created using the same init.
     * Use to cleanup memory for example on low memory alert from system
     */
    fun dispose(vararg key: MachineKey<*, *>) = dispose(key.toSet())

    /**
     * Disposes all inactive machines
     * @see dispose
     */
    fun disposeInactive()

    companion object {
        /**
         * Creates a container where machines may be activated and deactivated
         * Check their [MachineLifecycle] passed to init to guess if machine is active or paused
         * That may come handy when implementing some view-paging or nav-bar views
         * when you want to preserve machine states on several pages but want to pause
         * some resource-intensive pending operations when machine is not "in focus"
         * @param init Machine init
         * @param initiallyActive Machines that are initially active. Defaults to first machine in [init]
         */
        fun some(
            init: Collection<MachineInit<*, *>>,
            initiallyActive: Set<MachineKey<*, *>> = setOf(init.first().key)
        ): ActiveMachineContainer = SomeActiveMachineContainer(init, initiallyActive)
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
    private var machines: Map<MachineKey<*, *>, ProxyStateMachine<*, *>> = emptyMap()

    /**
     * Machine lifecycle that is always started
     */
    private val lifecycle = object : MachineLifecycle {
        override fun getState() = MachineLifecycle.State.ACTIVE
        override fun hasObservers(): Boolean = false
        override fun addObserver(observer: MachineLifecycle.Observer) = Unit
        override fun removeObserver(observer: MachineLifecycle.Observer) = Unit
    }

    /**
     * Creates a proxy machine given [MachineInit] structure
     * @param onUiChanged UI-state change handler
     */
    private fun <G: Any, U: Any> MachineInit<G, U>.machine(onUiChanged: (key: MachineKey<*, *>, uiState: Any) -> Unit) = ProxyStateMachine(
        initialUiState,
        { init(lifecycle) },
        { onUiChanged(key, it) }
    )

    /**
     * Starts machines
     */
    override fun start(onUiChanged: (key: MachineKey<*, *>, uiState: Any) -> Unit) {
        machines = init.associate { i -> i.key to i.machine(onUiChanged) }
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
    private var machines: Map<MachineKey<*, *>, ActiveStateMachine<*, *>> = emptyMap()

    /**
     * Starts machines
     */
    override fun start(onUiChanged: (key: MachineKey<*, *>, uiState: Any) -> Unit) {
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
    override fun getActive(): Set<MachineKey<*, *>> {
        return machines.entries.filter { (_, machine) -> machine.isActive() }.map { it.key }.toSet()
    }

    /**
     * Sets active machine given the key
     */
    override fun setActive(keys: Set<MachineKey<*, *>>) {
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

    /**
     * Disposes machines. All machines by [keys] are deactivated disposed and dereferenced.
     * When activating again - a new machine is created using the same init.
     * Use to cleanup memory for example on low memory alert from system
     */
    override fun dispose(keys: Set<MachineKey<*, *>>) {
        machines.forEach { (key, machine) ->
            if (keys.contains(key)) {
                machine.dispose()
            }
        }
    }

    /**
     * Disposes all inactive machines
     * @see dispose
     */
    override fun disposeInactive() {
        dispose(machines.keys - getActive())
    }
}