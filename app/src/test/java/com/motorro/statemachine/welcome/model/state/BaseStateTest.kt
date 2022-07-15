package com.motorro.statemachine.welcome.model.state

import androidx.annotation.CallSuper
import androidx.lifecycle.SavedStateHandle
import com.motorro.commonstatemachine.CommonStateMachine
import com.motorro.statemachine.commonapi.resources.ResourceWrapper
import com.motorro.statemachine.welcome.data.WelcomeGesture
import com.motorro.statemachine.welcome.data.WelcomeUiState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

open class BaseStateTest {
    protected val stateMachine: CommonStateMachine<WelcomeGesture, WelcomeUiState> = mockk(relaxed = true)
    protected val factory: WelcomeStateFactory = mockk()
    protected val context: WelcomeContext = mockk()
    protected val resourceWrapper: ResourceWrapper = mockk()

    init {
        every { context.factory } returns factory
        every { context.savedStateHandle } returns SavedStateHandle()
        every { resourceWrapper.getString(any(), *anyVararg()) } returns R_STRING
    }

    protected companion object {
        const val R_STRING = "String"
    }

    @Before
    @CallSuper
    @OptIn(ExperimentalCoroutinesApi::class)
    open fun before() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    @CallSuper
    @OptIn(ExperimentalCoroutinesApi::class)
    open fun after() {
        Dispatchers.resetMain()
    }
}