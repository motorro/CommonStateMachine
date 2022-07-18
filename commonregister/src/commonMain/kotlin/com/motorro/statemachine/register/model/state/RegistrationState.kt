package com.motorro.statemachine.register.model.state

import com.motorro.statemachine.commoncore.log.Logger
import com.motorro.statemachine.register.data.RegisterDataState
import com.motorro.statemachine.register.data.RegisterGesture
import com.motorro.statemachine.register.usecase.Registration
import kotlinx.coroutines.launch

/**
 * Emulates login operation
 */
class RegistrationState(
    context: RegisterContext,
    private val data: RegisterDataState,
    private val register: Registration
) : RegisterState(context) {

    /**
     * Should have valid email at this point
     */
    private val email = requireNotNull(data.commonData.email) {
        "Email is not provided"
    }

    /**
     * Should have valid password at this point
     */
    private val password = requireNotNull(data.password) {
        "Password is not provided"
    }

    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        setUiState(renderer.renderRegistration(data))
        Logger.d("Registering user...")
        stateScope.launch {
            register(email, password)
            Logger.d("Registered. Transferring to complete screen...")
            host.complete(email)
        }
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: RegisterGesture) = when(gesture) {
        RegisterGesture.Back -> onBack()
        else -> super.doProcess(gesture)
    }

    private fun onBack() {
        Logger.d("Returning to password entry")
        setMachineState(factory.passwordEntry(data))
    }

    /**
     * [RegisterState] factory
     */
    class Factory(private val register: Registration) {
        operator fun invoke(
            context: RegisterContext,
            data: RegisterDataState
        ): RegisterState = RegistrationState(
            context,
            data,
            register
        )
    }
}