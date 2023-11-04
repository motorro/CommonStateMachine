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

import com.motorro.commonstatemachine.CommonStateMachine
import com.motorro.commonstatemachine.ProxyStateMachine
import com.motorro.commonstatemachine.lifecycle.MachineLifecycle

/**
 * A machine container. Holds proxied machines.
 * The [CG] and [CU] type binding are done via explicit cast to be able
 * to host heterogeneous machines
 * [MachineInit] binds the key with correct machine type
 * @param CG Child gesture system
 * @param CU Child UI-state system
 */
interface ProxyMachineContainer<CG: Any, CU: Any> {
    /**
     * Machine access
     */
    val machineAccess: MachineAccess<CG, CU>

    /**
     * Starts machines in the container
     * @param onUiChanged UI change handler from hosting state
     */
    fun start(onUiChanged: (key: MachineKey<out CG, out CU>, uiState: CU) -> Unit)

    /**
     * Clears contained machines
     */
    fun clear()

    companion object {
        /**
         * Creates a container where all machines run together and their [MachineLifecycle] is
         * always [MachineLifecycle.State.ACTIVE]
         * @param init Machine init
         */
        fun <CG: Any, CU: Any> allTogether(
            init: Collection<MachineInit<out CG, out CU>>
        ): ProxyMachineContainer<CG, CU> = AllTogetherMachineContainer(init)

        /**
         * Creates a container where machines may be activated and deactivated
         * Check their [MachineLifecycle] passed to init to guess if machine is active or paused
         * That may come handy when implementing some view-paging or nav-bar views
         * when you want to preserve machine states on several pages but want to pause
         * some resource-intensive pending operations when machine is not "in focus"
         * @param init Machine init
         * @param initiallyActive Machines that are initially active. Defaults to first machine in [init]
         */
        fun <CG: Any, CU: Any> some(
            init: Collection<MachineInit<out CG, out CU>>,
            initiallyActive: Set<MachineKey<out CG, out CU>> = setOf(init.first().key)
        ): ActiveMachineContainer<CG, CU> = SomeActiveMachineContainer(init, initiallyActive)
    }

    /**
     * Base machine container that binds gesture/ui type system with machines
     * The [CG] and [CU] type binding are done via explicit cast to be able
     * to host heterogeneous machines
     * [MachineInit] binds the key with correct machine type
     * @param CG Common gesture type
     * @param CU Common UI type
     * @param M Concrete machine type
     * @param init Initialisation structures to create machines
     */
    abstract class Base<CG: Any, CU: Any, M: CommonStateMachine<*, out CU>>(
        private val init: Collection<MachineInit<out CG, out CU>>
    ) : ProxyMachineContainer<CG, CU> {
        /**
         * Creates a specific machine for this container
         * @param init Initialization structure
         * @param onUiChanged UI change handler from hosting state
         */
        abstract fun create(
            init: MachineInit<out CG, out CU>,
            onUiChanged: (key: MachineKey<out CG, out CU>, uiState: CU) -> Unit
        ): M

        /**
         * Machines bound with keys
         */
        protected var machines: Map<MachineKey<out CG, out CU>, M> = emptyMap()

        /**
         * Machine access
         */
        override val machineAccess: MachineAccess<CG, CU> = object : MachineAccess<CG, CU> {
            /**
             * Keys collection
             */
            override val keys: Set<MachineKey<out CG, out CU>> get() = machines.keys

            /**
             * Processes machine gesture.
             * [MachineInit] and [key] bind types securely.
             * @param G Concrete gesture, subtype of th [CG]
             * @param key Machine key
             * @param gesture Gesture to process
             */
            @Suppress("UNCHECKED_CAST")
            override fun <G : CG> process(key: MachineKey<G, out CU>, gesture: G) {
                (machines[key] as? CommonStateMachine<G, *>)?.process(gesture)
            }

            /**
             * Retrieves UI state.
             * [MachineInit] and [key] bind types securely.
             * @param U Concrete UI state bound with the [key], subtype of CU
             * @param key Machine key
             */
            @Suppress("UNCHECKED_CAST")
            override fun <U : CU> getState(key: MachineKey<out CG, U>): U? {
                return machines[key]?.getUiState() as? U
            }
        }

        /**
         * Starts machines in the container
         * @param onUiChanged UI change handler from hosting state
         */
        override fun start(onUiChanged: (key: MachineKey<out CG, out CU>, uiState: CU) -> Unit) {
            machines = init.associate { i -> i.key to create(i, onUiChanged) }
            machines.forEach { (_, machine) -> machine.start() }
            doStart()
        }

        /**
         * A part of [start] template called after initial startup
         * Machines are created at this point
         */
        open fun doStart() = Unit

        /**
         * Clears contained machines
         */
        override fun clear() {
            machines.forEach { (_, machine) -> machine.clear() }
        }
    }
}

/**
 * Container that activates machine
 */
interface ActiveMachineContainer<CG : Any, CU : Any> : ProxyMachineContainer<CG, CU> {
    /**
     * Retrieves currently active machine key
     */
    fun getActive(): Set<MachineKey<out CG, out CU>>

    /**
     * Sets active machine given the keys
     */
    fun setActive(keys: Set<MachineKey<out CG, out CU>>)

    /**
     * Sets active machine given the key
     */
    fun setActive(vararg key: MachineKey<out CG, out CU>) = setActive(key.toSet())

    /**
     * Disposes machines. All machines by [keys] are deactivated disposed and dereferenced.
     * When activating again - a new machine is created using the same init.
     * Use to cleanup memory for example on low memory alert from system
     */
    fun dispose(keys: Set<MachineKey<out CG, out CU>>)

    /**
     * Disposes machines. All machines by [key] are deactivated disposed and dereferenced.
     * When activating again - a new machine is created using the same init.
     * Use to cleanup memory for example on low memory alert from system
     */
    fun dispose(vararg key: MachineKey<out CG, out CU>) = dispose(key.toSet())

    /**
     * Disposes all inactive machines
     * @see dispose
     */
    fun disposeInactive()
}

/**
 * All machines run in parallel without any lifecycle management
 * @param init Machine init
 */
internal class AllTogetherMachineContainer<CG: Any, CU: Any>(
    init: Collection<MachineInit<out CG, out CU>>
) : ProxyMachineContainer.Base<CG, CU, CommonStateMachine<out CG, out CU>>(init) {
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
     * Creates a proxy
     */
    private fun <G: Any, U: Any> MachineInit<G, U>.machine(onUiChanged: (MachineKey<G, U>, U) -> Unit) = ProxyStateMachine(
        initialUiState,
        { init(lifecycle) },
        { onUiChanged(key, it) }
    )

    /**
     * Creates a specific machine for this container
     * @param init Initialization structure
     * @param onUiChanged UI change handler from hosting state
     */
    override fun create(
        init: MachineInit<out CG, out CU>,
        onUiChanged: (key: MachineKey<out CG, out CU>, uiState: CU) -> Unit
    ): CommonStateMachine<out CG, out CU> = init.machine(onUiChanged)
}

/**
 * Some machines run in active lifecycle state
 * The others are suspended when not active
 * @param init Machine init
 * @param initiallyActive Machines that are initially active.
 */
internal class SomeActiveMachineContainer<CG: Any, CU: Any>(
    private val init: Collection<MachineInit<out CG, out CU>>,
    private val initiallyActive: Set<MachineKey<out CG, out CU>>
) : ProxyMachineContainer.Base<CG, CU, ActiveStateMachine<out CG, out CU>>(init), ActiveMachineContainer<CG, CU> {
    /**
     * Creates a specific machine for this container
     * @param init Initialization structure
     * @param onUiChanged UI change handler from hosting state
     */
    override fun create(
        init: MachineInit<out CG, out CU>,
        onUiChanged: (key: MachineKey<out CG, out CU>, uiState: CU) -> Unit
    ): ActiveStateMachine<out CG, out CU> = ActiveStateMachine(init, onUiChanged)

    /**
     * A part of [start] template called after initial startup
     * Machines are created at this point
     */
    override fun doStart() {
        setActive(initiallyActive)
    }

    /**
     * Retrieves currently active machine key
     */
    override fun getActive(): Set<MachineKey<out CG, out CU>> = machines.entries
        .filter { (_, machine) -> machine.isActive() }
        .map { it.key }.toSet()

    /**
     * Sets active machine given the key
     */
    override fun setActive(keys: Set<MachineKey<out CG, out CU>>) {
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
    override fun dispose(keys: Set<MachineKey<out CG, out CU>>) {
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