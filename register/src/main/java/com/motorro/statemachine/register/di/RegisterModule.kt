/*
 * Copyright 2022 Nikolai Kotchetkov.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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