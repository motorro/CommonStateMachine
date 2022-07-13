package com.motorro.statemachine.login.di

import com.motorro.statemachine.login.model.state.LoginStateFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn

@Module
@InstallIn(LoginComponent::class)
interface LoginModule {
    @Binds
    @LoginScope
    fun factory(impl: LoginStateFactory.Impl): LoginStateFactory
}