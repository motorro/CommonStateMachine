package com.motorro.commonstatemachine.lifecycle

import com.motorro.commonstatemachine.multi.Activated

/**
 * A [MachineLifecycle] with explicit activation
 */
class ActivatedMachineLifecycle(startIn: MachineLifecycle.State) : MachineLifecycle, Activated {

    private val impl = BaseMachineLifecycle(startIn)

    override fun isActive(): Boolean = MachineLifecycle.State.ACTIVE == impl.getState()

    override fun activate() {
        impl.setState(MachineLifecycle.State.ACTIVE)
    }

    override fun deactivate() {
        impl.setState(MachineLifecycle.State.PAUSED)
    }

    override fun getState(): MachineLifecycle.State = impl.getState()
    override fun hasObservers(): Boolean = impl.hasObservers()
    override fun addObserver(observer: MachineLifecycle.Observer) = impl.addObserver(observer)
    override fun removeObserver(observer: MachineLifecycle.Observer) = impl.removeObserver(observer)
}