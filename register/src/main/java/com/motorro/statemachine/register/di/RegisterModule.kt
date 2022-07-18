package com.motorro.statemachine.register.di

import com.motorro.statemachine.commonapi.welcome.model.state.WelcomeFeatureHost
import com.motorro.statemachine.commoncore.coroutines.DispatcherProvider
import com.motorro.statemachine.commoncore.resources.ResourceWrapper
import com.motorro.statemachine.register.model.RegistrationRenderer
import com.motorro.statemachine.register.model.state.RegisterStateFactory
import com.motorro.statemachine.register.model.state.RegistrationState
import com.motorro.statemachine.register.usecase.Registration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn

@Module
@InstallIn(RegisterComponent::class)
class RegisterModule {
    @Provides
    @RegisterScope
    fun registration(dispatchers: DispatcherProvider): Registration = Registration.Impl(dispatchers)

    @Provides
    @RegisterScope
    fun registrationState(registration: Registration): RegistrationState.Factory = RegistrationState.Factory(registration)

    @Provides
    @RegisterScope
    fun renderer(resourceWrapper: ResourceWrapper): RegistrationRenderer = RegistrationRenderer.Impl(resourceWrapper)

    @Provides
    @RegisterScope
    fun factory(
        host: WelcomeFeatureHost,
        renderer: RegistrationRenderer,
        createRegistration: RegistrationState.Factory
    ): RegisterStateFactory = RegisterStateFactory.Impl(
        host,
        renderer,
        createRegistration
    )
}