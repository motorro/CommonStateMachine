package com.motorro.statemachine.login.model.state

import androidx.annotation.CallSuper
import com.motorro.statemachine.login.data.LoginGesture
import com.motorro.statemachine.login.data.LoginUiState
import com.motorro.statemachine.machine.CommonStateMachine
import com.motorro.statemachine.registrationapi.model.state.RegistrationFeatureHost
import com.motorro.statemachine.resources.ResourceWrapper
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
    protected val stateMachine: CommonStateMachine<LoginGesture, LoginUiState> = mockk(relaxed = true)
    protected val factory: LoginStateFactory = mockk()
    protected val host: RegistrationFeatureHost = mockk()
    protected val context: LoginContext = mockk()
    protected val resourceWrapper: ResourceWrapper = mockk()

    init {
        every { context.factory } returns factory
        every { context.host } returns host
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