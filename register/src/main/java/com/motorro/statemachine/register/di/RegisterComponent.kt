package com.motorro.statemachine.register.di

import com.motorro.statemachine.commonapi.welcome.model.state.WelcomeFeatureHost
import dagger.BindsInstance
import dagger.hilt.DefineComponent
import dagger.hilt.components.SingletonComponent

@RegisterScope
@DefineComponent(parent = SingletonComponent::class)
interface RegisterComponent

@DefineComponent.Builder
interface RegisterComponentBuilder {
    /**
     * Defines feature host graph
     */
    fun host(@BindsInstance value: WelcomeFeatureHost): RegisterComponentBuilder

    /**
     * Builds component
     */
    fun build(): RegisterComponent
}
