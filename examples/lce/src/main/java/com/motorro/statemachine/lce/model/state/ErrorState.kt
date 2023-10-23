/*
 * Copyright 2022 Nikolai Kotchetkov.
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

package com.motorro.statemachine.lce.model.state

import com.motorro.statemachine.lce.data.ItemId
import com.motorro.statemachine.lce.data.LceGesture
import com.motorro.statemachine.lce.data.LceUiState
import timber.log.Timber

/**
 * Item load error state
 * Processes [LceGesture.Back] to transfer back to list
 * Processes [LceGesture.Retry] to retry load
 * Processes [LceGesture.Exit] to exit flow
 * @param error Load error
 */
class ErrorState(private val failed: ItemId, private val error: Throwable) : LceLogicalState() {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        Timber.d("Displaying error...")
        setUiState(LceUiState.Error(error))
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: LceGesture) = when (gesture) {
        LceGesture.Back -> onBack()
        LceGesture.Retry -> onRetry()
        LceGesture.Exit -> onExit()
        else -> super.doProcess(gesture)
    }

    private fun onRetry() {
        Timber.d("Retry: retrying load...")
        setMachineState(LoadingState(failed))
    }

    private fun onBack() {
        Timber.d("Back: returning to item list view...")
        setMachineState(ItemListState())
    }

    private fun onExit() {
        Timber.d("Exit: Terminating flow...")
        setMachineState(TerminatedState())
    }
}