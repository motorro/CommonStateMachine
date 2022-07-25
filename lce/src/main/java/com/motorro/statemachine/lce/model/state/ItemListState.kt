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
import com.motorro.statemachine.lce.data.ItemModel
import com.motorro.statemachine.lce.data.LceGesture
import com.motorro.statemachine.lce.data.LceUiState
import timber.log.Timber

/**
 * Item list
 * Processes [LceGesture.ItemClicked] and transfers to loading state
 */
class ItemListState : LceLogicalState() {

    private val items = listOf(
        ItemId.LOADS_CONTENT to "Item that loads",
        ItemId.FAILS_WITH_ERROR to "Item that fails to load"
    )

    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        Timber.d("Displaying item list")
        setUiState(LceUiState.ItemList(items.map { ItemModel(it.first, it.second) }))
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: LceGesture) = when(gesture) {
        is LceGesture.ItemClicked -> onItemClicked(gesture.id)
        is LceGesture.Back -> onBack()
        else -> super.doProcess(gesture)
    }

    private fun onItemClicked(id: ItemId) {
        Timber.d("Item clicked: transferring to loading...")
        setMachineState(LoadingState(id))
    }

    private fun onBack() {
        Timber.d("Back: Terminating flow...")
        setMachineState(TerminatedState())
    }
}