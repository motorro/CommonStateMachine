package com.motorro.commonstatemachine

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class LiveDataStateMachineTest {

    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private class TestMachine(state: CommonMachineState<Int, Int>) : LiveDataStateMachine<Int, Int>( { state } )
    private val observer: Observer<Int> = mockk(relaxed = true)
    private val state = object : CommonMachineState<Int, Int>() {
        fun update() {
            setUiState(10)
        }
    }

    @Test
    fun updatesLiveData() {
        val machine = TestMachine(state)
        machine.uiState.observeForever(observer)
        state.update()

        verify { observer.onChanged(10) }
    }
}