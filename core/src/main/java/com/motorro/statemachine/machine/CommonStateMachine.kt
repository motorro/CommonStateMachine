package com.motorro.statemachine.machine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

/**
 * Common state-machine state
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

/**
 * Common state machine
 * @param U UI state class
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
     * @param Any UI state
     */
    fun setUiState(Any: U)

    /**
     * Cleans-up state-machine
     */
    fun clear()
}

/**
 * A common state-machine with live-data
 * @param U UI state class
 */
open class LiveDataStateMachine<G: Any, U: Any>(init: () -> CommonMachineState<G, U>): CommonStateMachine<G, U> {
    /**
     * State mediator
     */
    private val mediator = MutableLiveData<U>()

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
     * Updates state with UI gesture
     * @param gesture UI gesture to proceed
     */
    final override fun process(gesture: G) {
        machineState.process(gesture)
    }

    /**
     * Flow state
     */
    val uiState: LiveData<U> = Transformations.distinctUntilChanged(mediator)

    init {
        startMachineState()
    }

    /**
     * Updates UI state
     * @param Any UI state
     */
    final override fun setUiState(Any: U) {
        mediator.value = Any
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
