package com.motorro.statemachine.register.model.state

import com.motorro.statemachine.register.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

open class BaseStateTest {
    protected val stateMachine: MachineMock = MachineMock()
    protected val context: MockContext = MockContext()
    protected val factory: MockFactory = context.factory
    protected val host: MockHost = context.host
    protected val resourceWrapper: MockResourceWrapper = MockResourceWrapper()

    @BeforeTest
    @OptIn(ExperimentalCoroutinesApi::class)
    open fun before() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    @OptIn(ExperimentalCoroutinesApi::class)
    open fun after() {
        Dispatchers.resetMain()
    }
}