package com.motorro.statemachine.registration.di

import com.motorro.statemachine.registration.model.state.RegistrationStateFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface RegistrationModule {
    @Binds
    @ViewModelScoped
    fun factory(impl: RegistrationStateFactory.Impl): RegistrationStateFactory
}