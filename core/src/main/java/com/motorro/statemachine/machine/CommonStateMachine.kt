package com.motorro.statemachine.machine

/**
 * Common state machine
 * @param G UI gesture
 * @param U UI state
 */
interface CommonStateMachine<G: Any, U: Any> {
    /**
     * Active machine state
     */
    var machineState: CommonMachineState<G, U>

    /**
     * Updates state with UI gesture
     * @param gesture UI gesture to proceed
     */
    fun process(gesture: G)

    /**
     * Updates UI state
     * @param uiState UI state
     */
    fun setUiState(uiState: U)

    /**
     * Cleans-up state-machine
     */
    fun clear()

    /**
     * Base state-machine implementation
     * @param G UI gesture
     * @param U UI state
     * @param init Initial state producer
     */
    abstract class Base<G: Any, U: Any>(init: () -> CommonMachineState<G, U>) : CommonStateMachine<G, U> {
        /**
         * Active machine state
         */
        final override var machineState: CommonMachineState<G, U> = init()
            set(value) {
                clear()
                field = value
                startMachineState()
            }

        /**
         * Starts machine
         */
        protected fun start() {
            startMachineState()
        }

        /**
         * Updates state with UI gesture
         * @param gesture UI gesture to proceed
         */
        final override fun process(gesture: G) {
            machineState.process(gesture)
        }

        /**
         * Cleans-up state-machine
         */
        final override fun clear() {
            machineState.clear()
        }

        /**
         * Starts machine state
         */
        private fun startMachineState() {
            machineState.start(this)
        }
    }
}

