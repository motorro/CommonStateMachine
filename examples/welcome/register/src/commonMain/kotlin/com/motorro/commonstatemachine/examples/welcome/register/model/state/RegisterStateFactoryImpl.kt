package com.motorro.commonstatemachine.examples.welcome.register.model.state

import com.motorro.commonstatemachine.examples.commoncore.log.Logger
import com.motorro.commonstatemachine.examples.welcome.commonapi.model.state.WelcomeFeatureHost
import com.motorro.commonstatemachine.examples.welcome.register.RegisterFlowStarter
import com.motorro.commonstatemachine.examples.welcome.register.data.RegisterDataState
import com.motorro.commonstatemachine.examples.welcome.register.model.RegistrationRenderer
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam

/**
 * [RegisterStateFactory] implementation
 */
@Factory(binds = [RegisterFlowStarter::class])
internal class RegisterStateFactoryImpl(
    @InjectedParam host: WelcomeFeatureHost,
    renderer: RegistrationRenderer,
    private val createRegistration: RegistrationState.StateFactory
) : RegisterStateFactory {

    private val context: RegisterContext = object : RegisterContext {
        override val factory: RegisterStateFactory = this@RegisterStateFactoryImpl
        override val host: WelcomeFeatureHost = host
        override val renderer: RegistrationRenderer = renderer
    }

    /**
     * Password entry screen
     * @param data Registration data state
     */
    override fun passwordEntry(data: RegisterDataState): RegisterState {
        Logger.d("Creating 'Password entry'...")
        return PasswordEntryState(context, data)
    }

    /**
     * Registers user
     * @param data Data state
     */
    override fun registering(data: RegisterDataState): RegisterState {
        Logger.d("Creating 'Credentials check'...")
        return createRegistration(context, data)
    }
}