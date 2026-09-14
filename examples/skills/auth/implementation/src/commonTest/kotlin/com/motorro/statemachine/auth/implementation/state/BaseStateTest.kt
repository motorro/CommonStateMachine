package com.motorro.statemachine.auth.implementation.state

import com.motorro.commonstatemachine.CommonStateMachine
import com.motorro.commonstatemachine.flow.data.CommonFlowHost
import com.motorro.statemachine.auth.api.AuthResult
import com.motorro.statemachine.auth.implementation.data.AuthGestureImpl
import com.motorro.statemachine.auth.implementation.data.AuthUiStateImpl
import com.motorro.statemachine.auth.implementation.ui.AuthUiRenderer
import dev.mokkery.MockMode
import dev.mokkery.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@OptIn(ExperimentalCoroutinesApi::class)
internal abstract class BaseStateTest {
    protected lateinit var stateMachine: CommonStateMachine<AuthGestureImpl, AuthUiStateImpl>
    protected lateinit var stateFactory: AuthStateFactory
    protected lateinit var renderer: AuthUiRenderer
    protected lateinit var context: AuthContext
    protected lateinit var flowHost: CommonFlowHost<AuthResult>
    protected lateinit var nextState: AuthState
    protected lateinit var dispatcher: TestDispatcher

    @BeforeTest
    fun init() {
        dispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(dispatcher)

        stateMachine = mock(mode = MockMode.autoUnit)
        stateFactory = mock()
        flowHost = mock(mode = MockMode.autoUnit)
        renderer = mock(mode = MockMode.strict)
        nextState = AuthState()

        context = object : AuthContext {
            override val factory: AuthStateFactory = this@BaseStateTest.stateFactory
            override val flowHost: CommonFlowHost<AuthResult> = this@BaseStateTest.flowHost
            override val renderer: AuthUiRenderer = this@BaseStateTest.renderer
        }

        doInit()
    }

    @AfterTest
    fun deinit() {
        Dispatchers.resetMain()
    }

    protected fun test(block: TestScope.() -> Unit) = runTest(
        dispatcher,
        testBody = block
    )

    protected open fun doInit() = Unit
}