package com.motorro.commonstatemachine.lifecycle

import com.motorro.commonstatemachine.multi.Activated

/**
 * A [LifecycleState] with explicit activation
 */
class ActivatedLifecycleState(startIn: LifecycleState.State) : LifecycleState, Activated {

    private val impl = BaseLifecycleState(startIn)

    override fun isActive(): Boolean = LifecycleState.State.ACTIVE == impl.getState()

    override fun activate() {
        impl.setState(LifecycleState.State.ACTIVE)
    }

    override fun deactivate() {
        impl.setState(LifecycleState.State.PAUSED)
    }

    override fun getState(): LifecycleState.State = impl.getState()
    override fun hasObservers(): Boolean = impl.hasObservers()
    override fun addObserver(observer: LifecycleState.Observer) = impl.addObserver(observer)
    override fun removeObserver(observer: LifecycleState.Observer) = impl.removeObserver(observer)
}