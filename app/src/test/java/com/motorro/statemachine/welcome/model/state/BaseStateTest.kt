package com.motorro.statemachine.welcome.model.state

import androidx.annotation.CallSuper
import androidx.lifecycle.SavedStateHandle
import com.motorro.commonstatemachine.CommonStateMachine
import com.motorro.statemachine.commonapi.resources.ResourceWrapper
import com.motorro.statemachine.welcome.data.WelcomeGesture
import com.motorro.statemachine.welcome.data.WelcomeUiState
import com.motorro.statemachine.welcome.model.WelcomeRenderer
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
    protected val renderer: WelcomeRenderer = mockk()
    protected val resourceWrapper: ResourceWrapper = mockk()

    init {
        every { renderer.renderPreloading() } returns R_CONTENT
        every { renderer.renderTerms(any(), any(), any()) } returns R_CONTENT
        every { renderer.renderEmailEntry(any(), any()) } returns R_CONTENT
        every { renderer.renderChecking(any()) } returns R_CONTENT
        every { renderer.renderComplete(any()) } returns R_CONTENT
        every { context.factory } returns factory
        every { context.savedStateHandle } returns SavedStateHandle()
        every { context.renderer } returns renderer
        every { resourceWrapper.getString(any(), *anyVararg()) } returns R_STRING
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

    protected companion object {
        const val R_STRING = "String"
        val R_CONTENT = WelcomeUiState.Loading
    }
}