package com.motorro.commonstatemachine

class StateMock<G: Any, U: Any> : CommonMachineState<G, U>() {
    var started = false
    val processed = mutableListOf<G>()
    var cleared = false

    override fun doStart() {
        started = true
    }

    override fun doProcess(gesture: G) {
        processed.add(gesture)
    }

    override fun doClear() {
        cleared = true
    }
}

class MachineMock<G: Any, U: Any> : CommonStateMachine<G, U> {
    val machineStates = mutableListOf<CommonMachineState<G, U>>()
    val processed = mutableListOf<G>()
    val uiStates = mutableListOf<U>()
    var cleared = false

    override fun setMachineState(machineState: CommonMachineState<G, U>) {
        machineStates.add(machineState)
    }

    override fun process(gesture: G) {
        processed.add(gesture)
    }

    override fun setUiState(uiState: U) {
        uiStates.add(uiState)
    }

    override fun clear() {
        cleared = true
    }
}