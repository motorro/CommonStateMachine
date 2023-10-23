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
import com.motorro.commonstatemachine.CommonStateMachine
import com.motorro.commonstatemachine.ProxyMachineState
import com.motorro.commonstatemachine.ProxyStateMachine
import com.motorro.commonstatemachine.lifecycle.ActivatedMachineLifecycle
import com.motorro.commonstatemachine.lifecycle.MachineLifecycle

/**
 * State-machine wrapper that activates and deactivates
 * When disposed, stands-by but able to re-create the machine again
 * @param init Machine init
 * @param onUiChanged UI-state change handler
 */
internal class ActiveStateMachine<G: Any, U: Any>(
    init: MachineInit<G, U>,
    onUiChanged: (MachineKey<*, *>, Any) -> Unit
) : CommonStateMachine<G, U>, Activated {
    /**
     * Machine lifecycle
     */
    private val lifecycle = ActivatedMachineLifecycle(MachineLifecycle.State.PAUSED)

    private var machine = ProxyStateMachine(
        ActiveMachineUiState(init.initialUiState),
        { StandingBy(init, lifecycle) },
        { onUiChanged(init.key, it.child) }
    )

    init {
        machine.start()
    }

    override fun isActive(): Boolean = lifecycle.isActive()

    override fun activate() {
        lifecycle.activate()
        machine.process(ActiveMachineGesture.Created)
    }

    override fun deactivate() {
        lifecycle.deactivate()
    }

    /**
     * Disposes current machine
     */
    fun dispose() {
        deactivate()
        machine.process(ActiveMachineGesture.Disposed)
    }

    override fun process(gesture: G) = machine.process(ActiveMachineGesture.Child(gesture))
    override fun clear() = machine.clear()
    override fun isStarted(): Boolean = machine.isStarted()
    override fun getUiState(): U = machine.getUiState().child
    override fun setUiState(uiState: U) {
        throw UnsupportedOperationException("Setting UI state externally is not supported")
    }
    override fun setMachineState(machineState: CommonMachineState<G, U>) {
        throw UnsupportedOperationException("Setting Machine state externally is not supported")
    }
}

/**
 * Gesture for machine proxy
 */
private sealed class ActiveMachineGesture<out G : Any> {
    data object Created : ActiveMachineGesture<Nothing>()
    data object Disposed : ActiveMachineGesture<Nothing>()
    data class Child<G : Any>(val gesture: G): ActiveMachineGesture<G>()
}

/**
 * UI-state for machine proxy
 */
private data class ActiveMachineUiState<U : Any>(val child: U)

/**
 * Proxy machine is running
 */
private class Running<CG: Any, CU: Any>(
    private val init: MachineInit<CG, CU>,
    private val ls: ActivatedMachineLifecycle
) : ProxyMachineState<ActiveMachineGesture<CG>, ActiveMachineUiState<CU>, CG, CU>(init.initialUiState) {

    override fun init(): CommonMachineState<CG, CU> = init.init(ls)
    override fun mapUiState(child: CU): ActiveMachineUiState<CU> = ActiveMachineUiState(child)

    override fun doProcess(gesture: ActiveMachineGesture<CG>) = when(gesture) {
        ActiveMachineGesture.Disposed -> {
            setMachineState(StandingBy(init, ls))
        }
        is ActiveMachineGesture.Child -> super.doProcess(gesture)
        else -> Unit
    }

    override fun mapGesture(parent: ActiveMachineGesture<CG>): CG? = when(parent) {
        is ActiveMachineGesture.Child -> parent.gesture
        else -> null
    }
}

/**
 * No proxy machine, but ready to create one when activated
 */
private class StandingBy<CG: Any, CU: Any>(
    private val init: MachineInit<CG, CU>,
    private val ls: ActivatedMachineLifecycle
) : CommonMachineState<ActiveMachineGesture<CG>, ActiveMachineUiState<CU>>() {

    override fun doProcess(gesture: ActiveMachineGesture<CG>) {
        if(gesture is ActiveMachineGesture.Created) {
            setMachineState(Running(init, ls))
        }
    }
}

