package com.motorro.statemachine.registration.model.state

import androidx.annotation.CallSuper
import com.motorro.statemachine.machine.CommonStateMachine
import com.motorro.statemachine.registration.data.RegistrationGesture
import com.motorro.statemachine.registration.data.RegistrationUiState
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
    protected val stateMachine: CommonStateMachine<RegistrationGesture, RegistrationUiState> = mockk(relaxed = true)
    protected val factory: RegistrationStateFactory = mockk()
    protected val context: RegistrationContext = mockk()
    protected val resourceWrapper: ResourceWrapper = mockk()

    init {
        every { context.factory } returns factory
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