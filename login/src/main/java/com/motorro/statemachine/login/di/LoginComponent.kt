package com.motorro.statemachine.login.di

import com.motorro.statemachine.registrationapi.model.state.RegistrationFeatureHost
import dagger.BindsInstance
import dagger.hilt.DefineComponent
import dagger.hilt.components.SingletonComponent

@LoginScope
@DefineComponent(parent = SingletonComponent::class)
interface LoginComponent

@DefineComponent.Builder
interface LoginComponentBuilder {
    /**
     * Defines feature host graph
     */
    fun host(@BindsInstance value: RegistrationFeatureHost): LoginComponentBuilder

    /**
     * Builds component
     */
    fun build(): LoginComponent
}
