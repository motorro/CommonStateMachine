package com.motorro.commonstatemachine.lifecycle

/**
 * Combines [parent] state with [child] using [mixer]
 */
internal class CombineLifecycleState(
    private val parent: LifecycleState,
    private val child: LifecycleState,
    private val mixer: (LifecycleState.State, LifecycleState.State) -> LifecycleState.State
): LifecycleState {

    /**
     * State observers
     * Notified when state changes
     */
    private var observers = setOf<LifecycleState.Observer>()

    /**
     * Latest state sent to observers
     * Set to null when unsubscribed along with unsubscribing upstreams
     * If none listens - we don't care about updates from the upstream
     */
    private var latestUpdate: LifecycleState.State? = null

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
    private val upstreamObserver = LifecycleState.Observer {
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

    override fun getState(): LifecycleState.State = getCombinedState()
    override fun hasObservers(): Boolean = observers.isNotEmpty()
    override fun addObserver(observer: LifecycleState.Observer) {
        if (hasObservers().not()) {
            subscribe()
        }
        observers = observers.plus(observer)
    }
    override fun removeObserver(observer: LifecycleState.Observer) {
        observers = observers.minus(observer)
        if (hasObservers().not()) {
            unsubscribe()
        }
    }
}