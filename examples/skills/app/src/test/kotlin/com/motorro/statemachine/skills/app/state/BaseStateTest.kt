package com.motorro.statemachine.skills.app.state

import com.motorro.commonstatemachine.CommonStateMachine
import com.motorro.commonstatemachine.flow.data.CommonFlowHost
import com.motorro.statemachine.skills.app.data.MainGesture
import com.motorro.statemachine.skills.app.data.MainUiState
import com.motorro.statemachine.skills.app.ui.MainUiRenderer
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

@OptIn(ExperimentalCoroutinesApi::class)
internal abstract class BaseStateTest {
    protected lateinit var stateMachine: CommonStateMachine<MainGesture, MainUiState>
    protected lateinit var stateFactory: MainStateFactory
    protected lateinit var renderer: MainUiRenderer
    protected lateinit var context: MainContext
    protected lateinit var flowHost: CommonFlowHost<Unit>
    protected lateinit var nextState: MainState
    protected lateinit var dispatcher: TestDispatcher

    @Before
    fun init() {
        dispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(dispatcher)

        stateMachine = mockk(relaxed = true)
        stateFactory = mockk()
        flowHost = mockk(relaxed = true)
        renderer = mockk()
        nextState = MainState()

        context = object : MainContext {
            override val factory: MainStateFactory = this@BaseStateTest.stateFactory
            override val flowHost: CommonFlowHost<Unit> = this@BaseStateTest.flowHost
            override val renderer: MainUiRenderer = this@BaseStateTest.renderer
        }

        doInit()
    }

    @After
    fun deinit() {
        Dispatchers.resetMain()
    }

    protected fun test(block: TestScope.() -> Unit) = runTest(
        dispatcher,
        testBody = block
    )

    protected open fun doInit() = Unit
}