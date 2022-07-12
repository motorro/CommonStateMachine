package com.motorro.statemachine.machine

import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertTrue

internal class ProxyMachineStateTest {

    private val stateMachine: CommonStateMachine<String, String> = mockk(relaxed = true)

    private class TestProxyState(private val state: CommonMachineState<Int, Int>) : ProxyMachineState<String, String, Int, Int>() {
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
        val child = spyk(CommonMachineState<Int, Int>())
        val state = TestProxyState(child)

        state.start(stateMachine)
        state.process("1")

        verify { child.process(1) }
    }

    @Test
    fun ifGestureMapperReturnsNullDoesNotCallChild() {
        val child = spyk(CommonMachineState<Int, Int>())
        val state = TestProxyState(child)

        state.start(stateMachine)
        state.process("-1")

        verify(exactly = 0) { child.process(any()) }
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

        verify { stateMachine.setUiState("1") }
    }

    @Test
    fun cleansUpChildState() {
        val child = spyk(CommonMachineState<Int, Int>())
        val state = TestProxyState(child)

        state.start(stateMachine)
        state.clear()

        verify { child.clear() }
    }
}