package com.motorro.statemachine

import com.motorro.statemachine.coroutines.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object TestDispatchers : DispatcherProvider {
    override val default: CoroutineDispatcher = Dispatchers.Main
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val io: CoroutineDispatcher = Dispatchers.Main
}