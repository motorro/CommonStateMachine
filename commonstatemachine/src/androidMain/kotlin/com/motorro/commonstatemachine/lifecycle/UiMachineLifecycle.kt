package com.motorro.commonstatemachine.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.motorro.commonstatemachine.lifecycle.UiMachineLifecycle.Companion.bindLifecycle

/**
 * Watches for Android lifecycle changes.
 * - Put to your `ViewModel` and provide to your states
 * - Call [bindLifecycle] to get Android Lifecycle observer
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
     * UI observer
     */
    interface UiObserver : LifecycleObserver {
        /**
         * Disposes observer
         */
        fun dispose()
    }

    /**
     * Observes lifecycle
     */
    private inner class UiObserverImpl(private val lifecycle: Lifecycle) : DefaultLifecycleObserver, UiObserver {
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

        override fun dispose() {
            lifecycle.removeObserver(this)
        }

        override fun onStart(owner: LifecycleOwner) {
            emitState()
        }
        override fun onStop(owner: LifecycleOwner) {
            emitState()
        }
        override fun onDestroy(owner: LifecycleOwner) {
            dispose()
        }
    }

    companion object {
        /**
         * Binds lifecycle observer
         * Call from your view component
         */
        fun UiMachineLifecycle.bindLifecycle(lifecycle: Lifecycle): UiObserver = UiObserverImpl(lifecycle)
    }
}