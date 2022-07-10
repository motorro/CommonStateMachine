package com.motorro.statemachine.registration.model.state

import com.motorro.statemachine.machine.CommonStateMachine
import com.motorro.statemachine.registration.data.RegistrationGesture
import com.motorro.statemachine.registration.data.RegistrationUiState
import com.motorro.statemachine.resources.ResourceWrapper
import io.mockk.every
import io.mockk.mockk

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
}