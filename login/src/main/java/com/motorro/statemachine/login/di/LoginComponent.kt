package com.motorro.statemachine.login.di

import com.motorro.statemachine.commonapi.welcome.model.state.WelcomeFeatureHost
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
    fun host(@BindsInstance value: WelcomeFeatureHost): LoginComponentBuilder

    /**
     * Builds component
     */
    fun build(): LoginComponent
}
