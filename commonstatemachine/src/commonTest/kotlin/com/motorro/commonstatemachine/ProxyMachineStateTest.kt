package com.motorro.commonstatemachine

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class ProxyMachineStateTest {

    private val stateMachine = MachineMock<String, String>()

    private class TestProxyState(private val state: CommonMachineState<Int, Int>) : com.motorro.commonstatemachine.ProxyMachineState<String, String, Int, Int>() {
        override fun init(): CommonMachineState<Int, Int> = state
        override fun mapGesture(parent: String): Int? = parent.toInt().takeIf { it >= 0 }
        override fun mapUiState(child: Int): String = child.toString()
    }

    @Test
    fun startsInitialStateOnStart() {
        val child = object : CommonMachineState<Int, Int>() {
            var started = false

            override fun doStart() {
                started = true
            }
        }
        val state = TestProxyState(child)

        state.start(stateMachine)

        assertTrue { child.started }
    }

    @Test
    fun delegatesGestureToCurrentState() {
        val child = StateMock<Int, Int>()
        val state = TestProxyState(child)

        state.start(stateMachine)
        state.process("1")

        assertTrue { child.processed.contains(1) }
    }

    @Test
    fun ifGestureMapperReturnsNullDoesNotCallChild() {
        val child = StateMock<Int, Int>()
        val state = TestProxyState(child)

        state.start(stateMachine)
        state.process("-1")

        assertTrue { child.processed.isEmpty() }
    }

    @Test
    fun upstreamsUiChanges() {
        val child = object : CommonMachineState<Int, Int>() {
            fun update() {
                setUiState(1)
            }
        }
        val state = TestProxyState(child)

        state.start(stateMachine)
        child.update()

        assertTrue { stateMachine.uiStates.contains("1") }
    }

    @Test
    fun cleansUpChildState() {
        val child = StateMock<Int, Int>()
        val state = TestProxyState(child)

        state.start(stateMachine)
        state.clear()

        assertTrue { child.cleared }
    }
}