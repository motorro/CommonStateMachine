/*
 * Copyright 2023 Nikolai Kotchetkov.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.motorro.commonstatemachine.coroutines.lifecycle

import com.motorro.commonstatemachine.lifecycle.MachineLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

/**
 * Flow adapter for [MachineLifecycle]
 */
fun MachineLifecycle.asFlow(scope: CoroutineScope): StateFlow<MachineLifecycle.State> =
    callbackFlow {
        val observer  = object : MachineLifecycle.Observer {
            /**
             * Called when state changes
             */
            override fun onStateChange(state: MachineLifecycle.State) {
                trySend(state)
            }
        }

        addObserver(observer)

        awaitClose {
            removeObserver(observer)
        }
    }
    .stateIn(scope, SharingStarted.Lazily, getState())

/**
 * Callback adapter for [StateFlow]
 */
fun StateFlow<MachineLifecycle.State>.asCallback(coroutineScope: CoroutineScope): MachineLifecycle = object : MachineLifecycle {

    private val observers = mutableSetOf <MachineLifecycle.Observer>()
    private var job: Job? = null

    override fun getState(): MachineLifecycle.State = this@asCallback.value

    override fun hasObservers(): Boolean = observers.isNotEmpty()

    override fun addObserver(observer: MachineLifecycle.Observer) {
        observers.add(observer)
        if (1 == observers.size) {
            start()
        }
    }

    override fun removeObserver(observer: MachineLifecycle.Observer) {
        observers.remove(observer)
        if (observers.isEmpty()) {
            stop()
        }
    }

    private fun start() {
        job = this@asCallback.onEach(::update).launchIn(coroutineScope)
    }

    private fun stop() {
        job?.cancel()
        job = null
    }

    private fun update(state: MachineLifecycle.State) {
        observers.forEach { it.onStateChange(state) }
    }
}
