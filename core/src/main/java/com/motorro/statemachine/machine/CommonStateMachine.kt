package com.motorro.statemachine.machine

/**
 * Common state machine
 * @param G UI gesture
 * @param U UI state
 */
interface CommonStateMachine<G: Any, U: Any> {
    /**
     * Sets active machine state
     */
    fun setMachineState(machineState: CommonMachineState<G, U>)

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
    abstract class Base<G: Any, U: Any>(private val init: () -> CommonMachineState<G, U>) : CommonStateMachine<G, U> {

        /**
         * Active machine state
         */
        protected lateinit var activeState: CommonMachineState<G, U>

        /**
         * Start fuze
         */
        private var started: Boolean = false

        /**
         * Sets active machine state
         */
        final override fun setMachineState(machineState: CommonMachineState<G, U>) {
            clear()
            activeState = machineState
            startMachineState()
        }

        /**
         * Starts machine
         */
        protected fun start() {
            if (started.not()) {
                activeState = init()
                startMachineState()
                started = true
            }
        }

        /**
         * Updates state with UI gesture
         * @param gesture UI gesture to proceed
         */
        final override fun process(gesture: G) {
            activeState.process(gesture)
        }

        /**
         * Cleans-up state-machine
         */
        final override fun clear() {
            activeState.clear()
        }

        /**
         * Starts machine state
         */
        private fun startMachineState() {
            activeState.start(this)
        }
    }
}

