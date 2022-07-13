package com.motorro.commonstatemachine

/**
 * Proxy state that runs child state machine and maps gestures and results to parent
 * Use when you want to plug-in some child sub-graph of states to parent
 * @param PG Parent gesture
 * @param PU Parent UI state
 * @param CG Child gesture
 * @param CU Child UI state
 * @see mapUiState
 * @see mapGesture
 */
abstract class ProxyMachineState<PG: Any, PU: Any, CG: Any, CU: Any> : CommonMachineState<PG, PU>() {
    /**
     * Proxy state machine
     */
    private val machine = object : CommonStateMachine.Base<CG, CU>(::init) {

        fun doStart() {
            start()
        }

        override fun setUiState(uiState: CU) {
            this@ProxyMachineState.setUiState(mapUiState(uiState))
        }
    }

    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        machine.doStart()
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: PG) {
        mapGesture(gesture)?.let(machine::process)
    }

    /**
     * A part of [clear] template to clean-up state
     */
    override fun doClear() {
        machine.clear()
    }

    /**
     * Creates initial child state
     */
    protected abstract fun init(): CommonMachineState<CG, CU>

    /**
     * Maps child UI state to parent if relevant
     * @param parent Parent gesture
     * @return Mapped gesture or null if not applicable
     */
    protected abstract fun mapGesture(parent: PG): CG?

    /**
     * Maps child UI state to parent
     * @param child Child UI state
     */
    protected abstract fun mapUiState(child: CU): PU
}