package com.motorro.statemachine.commonapi.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Provides coroutines dispatchers
 */
interface DispatcherProvider {
    val default: CoroutineDispatcher
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
}

/**
 * Test dispatchers
 */
object TestDispatchers : DispatcherProvider {
    override val default: CoroutineDispatcher = Dispatchers.Main
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val io: CoroutineDispatcher = Dispatchers.Main
}