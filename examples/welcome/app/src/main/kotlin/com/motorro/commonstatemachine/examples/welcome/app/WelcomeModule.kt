package com.motorro.commonstatemachine.examples.welcome.app

import android.content.Context
import com.motorro.commonstatemachine.examples.commoncore.coroutines.DispatcherProvider
import com.motorro.commonstatemachine.examples.commoncore.resources.ResourceWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
@ComponentScan
class WelcomeModule {

    @Factory
    fun resourceWrapper(context: Context): ResourceWrapper = object : ResourceWrapper {
        override fun getString(resId: Int, vararg args: Any): String = context.getString(resId, *args)
    }

    @Factory
    fun dispatchers(): DispatcherProvider = object : DispatcherProvider {
        override val default: CoroutineDispatcher = Dispatchers.Default
        override val main: CoroutineDispatcher = Dispatchers.Main
        override val io: CoroutineDispatcher = Dispatchers.IO
    }
}