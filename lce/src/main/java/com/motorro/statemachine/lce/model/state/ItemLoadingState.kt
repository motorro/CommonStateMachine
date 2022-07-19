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

import androidx.annotation.VisibleForTesting
import com.motorro.commonstatemachine.CoroutineState
import com.motorro.statemachine.lce.data.ItemId
import com.motorro.statemachine.lce.data.LceGesture
import com.motorro.statemachine.lce.data.LceUiState
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.IOException

/**
 * Loads item contents
 * Processes [LceGesture.Back] to transfer back to list
 * Transfers to error or to detail view
 * @param id Item ID to load
 * @param defaultDispatcher Dispatcher to run load on (used for testing)
 */
class ItemLoadingState(
    @get:VisibleForTesting val id: ItemId,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : CoroutineState<LceGesture, LceUiState>() {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        Timber.d("Displaying spinner...")
        setUiState(LceUiState.Loading)
        load()
    }

    private fun load() {
        stateScope.launch(defaultDispatcher) {
            Timber.d("Loading data...")
            delay(1000L)
            withContext(Dispatchers.Main) {
                when (id) {
                    ItemId.LOADS_CONTENT -> toContent()
                    ItemId.FAILS_WITH_ERROR -> toError()
                }
            }
        }
    }

    private fun toContent() {
        Timber.d("Data loaded: transferring to content...")
        setMachineState(ItemDetailsState("Some item data..."))
    }

    private fun toError() {
        Timber.d("Data load error: transferring to error...")
        setMachineState(LoadErrorState(id, IOException("Failed to load item")))
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: LceGesture) = when(gesture) {
        LceGesture.Back -> onBack()
        else -> Timber.d("Unhandled gesture: %s", gesture)
    }

    private fun onBack() {
        Timber.d("Back: returning to item view")
        setMachineState(ItemListState())
    }
}