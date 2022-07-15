package com.motorro.statemachine.welcome.di

import com.motorro.statemachine.welcome.model.state.WelcomeStateFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface WelcomeModule {
    @Binds
    @ViewModelScoped
    fun factory(impl: WelcomeStateFactory.Impl): WelcomeStateFactory
}