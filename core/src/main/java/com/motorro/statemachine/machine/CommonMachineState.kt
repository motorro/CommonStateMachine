package com.motorro.statemachine.machine

/**
 * Common state-machine state
 * @param G UI gesture
 * @param U UI state
 */
open class CommonMachineState<G: Any, U : Any> {
    /**
     * Hosting state machine
     */
    private var machine: CommonStateMachine<G, U>? = null

    /**
     * Starts state
     */
    fun start(machine: CommonStateMachine<G, U>) {
        this.machine = machine
        doStart()
    }

    /**
     * A part of [start] template to initialize state
     */
    protected open fun doStart(): Unit = Unit

    /**
     * Updates state with UI gesture
     * @param gesture UI gesture to proceed
     */
    fun process(gesture: G): Unit = withMachine {
        doProcess(gesture)
    }

    /**
     * A part of [process] template to process UI gesture
     */
    protected open fun doProcess(gesture: G): Unit = Unit

    /**
     * Updates UI state
     * @param Any UI state
     */
    protected fun setUiState(Any: U) = withMachine {
        it.setUiState(Any)
    }

    /**
     * Updates machine state
     * @param machineState Machine state
     */
    protected fun setMachineState(machineState: CommonMachineState<G, U>) = withMachine {
        it.machineState = machineState
    }

    /**
     * Ensures machine is set
     * @param block Block to run on machine
     */
    private inline fun withMachine(block: (CommonStateMachine<G, U>) -> Unit) {
        val machine = checkNotNull(machine) { "Can't update Machine while not active" }
        block(machine)
    }

    /**
     * Called when state is removed from machine
     * Cleans-up the state
     */
    fun clear() {
        doClear()
        machine = null
    }

    /**
     * A part of [clear] template to clean-up state
     */
    protected open fun doClear(): Unit = Unit
}