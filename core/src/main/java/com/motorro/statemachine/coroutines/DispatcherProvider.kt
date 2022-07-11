package com.motorro.statemachine.coroutines

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Provides coroutines dispatchers
 */
interface DispatcherProvider {
    val default: CoroutineDispatcher
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
}