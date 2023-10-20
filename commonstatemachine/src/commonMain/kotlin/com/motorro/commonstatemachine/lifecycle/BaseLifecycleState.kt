package com.motorro.commonstatemachine.lifecycle

import kotlin.properties.Delegates

/**
 * Base lifecycle state to support subscribers
 */
internal class BaseLifecycleState(startIn: LifecycleState.State) : LifecycleState {

    private var observers = setOf<LifecycleState.Observer>()

    override fun hasObservers(): Boolean = observers.isNotEmpty()

    private var lifecycle: LifecycleState.State by Delegates.observable(startIn) { _, old, new ->
        if (new != old) {
            observers.forEach { it.onStateChange(new) }
        }
    }

    override fun getState(): LifecycleState.State = lifecycle

    fun setState(state: LifecycleState.State) {
        lifecycle = state
    }

    override fun addObserver(observer: LifecycleState.Observer) {
        observers = observers.plus(observer)
    }

    override fun removeObserver(observer: LifecycleState.Observer) {
        observers = observers.minus(observer)
    }
}