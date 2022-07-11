package com.motorro.statemachine.di

import android.app.Application
import android.content.Context
import com.motorro.statemachine.coroutines.DispatcherProvider
import com.motorro.statemachine.resources.ResourceWrapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun context(application: Application): Context = application

    @Provides
    @Singleton
    fun resourceWrapper(context: Context): ResourceWrapper = object : ResourceWrapper {
        override fun getString(resId: Int, vararg args: Any): String = context.getString(resId, *args)
    }

    @Provides
    @Singleton
    fun dispatchers(): DispatcherProvider = object : DispatcherProvider {
        override val default: CoroutineDispatcher = Dispatchers.Default
        override val main: CoroutineDispatcher = Dispatchers.Main
        override val io: CoroutineDispatcher = Dispatchers.IO
    }
}