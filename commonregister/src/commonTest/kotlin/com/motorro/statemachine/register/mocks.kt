package com.motorro.statemachine.register

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.CommonStateMachine
import com.motorro.statemachine.commonapi.resources.ResourceWrapper
import com.motorro.statemachine.register.data.PasswordValidationError
import com.motorro.statemachine.register.data.RegisterDataState
import com.motorro.statemachine.register.data.RegisterGesture
import com.motorro.statemachine.register.data.RegisterUiState
import com.motorro.statemachine.register.model.RegistrationRenderer
import com.motorro.statemachine.register.model.state.RegisterContext
import com.motorro.statemachine.register.model.state.RegisterState
import com.motorro.statemachine.register.model.state.RegisterStateFactory
import com.motorro.statemachine.register.usecase.Registration
import com.motorro.statemachine.welcome.data.WelcomeDataState
import com.motorro.statemachine.welcome.model.state.WelcomeFeatureHost

const val R_STRING = "string"
val R_CONTENT = RegisterUiState.Loading

class StateMock(context: RegisterContext) : RegisterState(context) {
    var started = false
    val processed = mutableListOf<RegisterGesture>()
    var cleared = false

    override fun doStart() {
        started = true
    }

    override fun doProcess(gesture: RegisterGesture) {
        processed.add(gesture)
    }

    override fun doClear() {
        cleared = true
    }
}

class MachineMock : CommonStateMachine<RegisterGesture, RegisterUiState> {
    val machineStates = mutableListOf<CommonMachineState<RegisterGesture, RegisterUiState>>()
    val processed = mutableListOf<RegisterGesture>()
    val uiStates = mutableListOf<RegisterUiState>()
    var cleared = false

    override fun setMachineState(machineState: CommonMachineState<RegisterGesture, RegisterUiState>) {
        machineStates.add(machineState)
    }

    override fun process(gesture: RegisterGesture) {
        processed.add(gesture)
    }

    override fun setUiState(uiState: RegisterUiState) {
        uiStates.add(uiState)
    }

    override fun clear() {
        cleared = true
    }
}

class MockContext : RegisterContext {
    override val factory: MockFactory = MockFactory(this)
    override val host: MockHost = MockHost()
    override val renderer: MockRenderer = MockRenderer()
}

class MockHost : WelcomeFeatureHost {
    val backToEmails = mutableListOf<WelcomeDataState>()
    val completes = mutableListOf<String>()

    override fun backToEmailEntry(data: WelcomeDataState) {
        backToEmails.add(data)
    }

    override fun complete(email: String) {
        completes.add(email)
    }
}

class MockFactory(private val context: RegisterContext) : RegisterStateFactory {
    val passwordEntries = mutableListOf<RegisterDataState>()
    val registers = mutableListOf<RegisterDataState>()

    override fun passwordEntry(data: RegisterDataState): RegisterState {
        passwordEntries.add(data)
        return StateMock(context)
    }

    override fun registering(data: RegisterDataState): RegisterState {
        registers.add(data)
        return StateMock(context)
    }
}

class MockRenderer : RegistrationRenderer {
    val passwordEntries = mutableListOf<Triple<RegisterDataState, String?, PasswordValidationError?>>()
    val registrations = mutableListOf<RegisterDataState>()

    override fun renderPasswordEntry(
        data: RegisterDataState,
        repeatPassword: String?,
        error: PasswordValidationError?
    ): RegisterUiState {
        passwordEntries.add(Triple(data, repeatPassword, error))
        return R_CONTENT
    }

    override fun renderRegistration(data: RegisterDataState): RegisterUiState {
        registrations.add(data)
        return R_CONTENT
    }
}

class MockResourceWrapper : ResourceWrapper {
    val getStrings = mutableListOf<Pair<Int, List<Any>>>()

    override fun getString(resId: Int, vararg args: Any): String {
        getStrings.add(resId to args.toList())
        return R_STRING
    }
}

class MockRegistration : Registration {
    val invocations = mutableListOf<Pair<String, String>>()

    override suspend fun invoke(email: String, password: String): Boolean {
        invocations.add(email to password)
        return true
    }
}