package com.motorro.commonstatemachine.examples.welcome.login.model.state

import com.motorro.commonstatemachine.examples.welcome.commonapi.model.state.WelcomeFeatureHost
import com.motorro.commonstatemachine.examples.welcome.login.LoginFlowStarter
import com.motorro.commonstatemachine.examples.welcome.login.data.LoginDataState
import com.motorro.commonstatemachine.examples.welcome.login.model.LoginRenderer
import io.github.aakira.napier.Napier
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam

/**
 * [LoginStateFactory] implementation
 */
@Factory(binds = [LoginFlowStarter::class])
internal class LoginStateFactoryImpl(
    @InjectedParam host: WelcomeFeatureHost,
    renderer: LoginRenderer,
    private val createCredentialsCheck: CredentialsCheckState.StateFactory
) : LoginStateFactory {

    private val context: LoginContext = object : LoginContext {
        override val factory: LoginStateFactory = this@LoginStateFactoryImpl
        override val host: WelcomeFeatureHost = host
        override val renderer: LoginRenderer = renderer
    }

    /**
     * Enter existing user password
     * @param data Common data state
     */
    override fun passwordEntry(data: LoginDataState): LoginState {
        Napier.d("Creating 'Password entry'...")
        return PasswordEntryState(context, data)
    }

    /**
     * Checks email/password
     * @param data Data state
     */
    override fun checking(data: LoginDataState): LoginState {
        Napier.d("Creating 'Credentials check'...")
        return createCredentialsCheck(context, data)
    }

    /**
     * Password error screen
     */
    override fun error(data: LoginDataState, error: Throwable): LoginState {
        Napier.d("Creating 'Error'...")
        return ErrorState(context, data, error)
    }
}