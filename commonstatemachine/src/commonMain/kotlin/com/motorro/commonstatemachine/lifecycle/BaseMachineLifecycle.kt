package com.motorro.commonstatemachine.lifecycle

import kotlin.properties.Delegates

/**
 * Base lifecycle state to support subscribers
 */
class BaseMachineLifecycle(startIn: MachineLifecycle.State) : MachineLifecycle {

    private var observers = setOf<MachineLifecycle.Observer>()

    override fun hasObservers(): Boolean = observers.isNotEmpty()

    private var lifecycle: MachineLifecycle.State by Delegates.observable(startIn) { _, old, new ->
        if (new != old) {
            observers.forEach { it.onStateChange(new) }
        }
    }

    override fun getState(): MachineLifecycle.State = lifecycle

    fun setState(state: MachineLifecycle.State) {
        lifecycle = state
    }

    override fun addObserver(observer: MachineLifecycle.Observer) {
        observers = observers.plus(observer)
    }

    override fun removeObserver(observer: MachineLifecycle.Observer) {
        observers = observers.minus(observer)
    }
}