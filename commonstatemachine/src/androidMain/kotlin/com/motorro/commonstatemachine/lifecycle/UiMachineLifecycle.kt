package com.motorro.commonstatemachine.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.motorro.commonstatemachine.lifecycle.UiMachineLifecycle.Companion.getObserver

/**
 * Watches for Android lifecycle changes.
 * - Put to your `ViewModel` and provide to your states
 * - Call [getObserver] to get Android Lifecycle observer
 * - Add your view lifecycle to observer to get lifecycle updates
 */
class UiMachineLifecycle : MachineLifecycle {
    /**
     * Lifecycle delegate
     */
    private var impl = BaseMachineLifecycle(MachineLifecycle.State.PAUSED)

    override fun getState(): MachineLifecycle.State = impl.getState()
    override fun hasObservers(): Boolean = impl.hasObservers()
    override fun addObserver(observer: MachineLifecycle.Observer) = impl.addObserver(observer)
    override fun removeObserver(observer: MachineLifecycle.Observer) = impl.removeObserver(observer)

    /**
     * Observes lifecycle
     */
    private inner class UIObserver(private val lifecycle: Lifecycle) : DefaultLifecycleObserver {
        init {
            emitState()
            lifecycle.addObserver(this)
        }

        private fun Lifecycle.State.toDomain() = when {
            isAtLeast(Lifecycle.State.STARTED) -> MachineLifecycle.State.ACTIVE
            else -> MachineLifecycle.State.PAUSED
        }

        private fun emitState() {
            impl.setState(lifecycle.currentState.toDomain())
        }

        override fun onStart(owner: LifecycleOwner) {
            emitState()
        }
        override fun onStop(owner: LifecycleOwner) {
            emitState()
        }
    }

    companion object {
        /**
         * Creates lifecycle observer
         * Add it to your view component
         */
        fun UiMachineLifecycle.getObserver(lifecycle: Lifecycle): LifecycleObserver = UIObserver(lifecycle)
    }
}