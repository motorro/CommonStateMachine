package com.motorro.commonstatemachine.lifecycle

/**
 * Combines [parent] state with [child] using [mixer]
 */
internal class CombineMachineLifecycle(
    private val parent: MachineLifecycle,
    private val child: MachineLifecycle,
    private val mixer: (MachineLifecycle.State, MachineLifecycle.State) -> MachineLifecycle.State
): MachineLifecycle {

    /**
     * State observers
     * Notified when state changes
     */
    private var observers = setOf<MachineLifecycle.Observer>()

    /**
     * Latest state sent to observers
     * Set to null when unsubscribed along with unsubscribing upstreams
     * If none listens - we don't care about updates from the upstream
     */
    private var latestUpdate: MachineLifecycle.State? = null

    /**
     * Updates listeners if subscribed.
     * Called when have active observers. Otherwise we should not be here as we unsubscribe upstream
     * if none listens
     */
    private fun updateListeners() {
        val newState = getCombinedState()
        if (latestUpdate != newState) {
            latestUpdate = newState
            observers.forEach { it.onStateChange(newState) }
        }
    }

    /**
     * Combines states
     */
    private fun getCombinedState() = mixer(parent.getState(), child.getState())

    // Update listeners
    private val upstreamObserver = MachineLifecycle.Observer {
        updateListeners()
    }

    private fun subscribe() {
        parent.addObserver(upstreamObserver)
        child.addObserver(upstreamObserver)
    }

    private fun unsubscribe() {
        parent.removeObserver(upstreamObserver)
        child.removeObserver(upstreamObserver)
        latestUpdate = null
    }

    override fun getState(): MachineLifecycle.State = getCombinedState()
    override fun hasObservers(): Boolean = observers.isNotEmpty()
    override fun addObserver(observer: MachineLifecycle.Observer) {
        if (hasObservers().not()) {
            subscribe()
        }
        observers = observers.plus(observer)
    }
    override fun removeObserver(observer: MachineLifecycle.Observer) {
        observers = observers.minus(observer)
        if (hasObservers().not()) {
            unsubscribe()
        }
    }
}