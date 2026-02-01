/*
 * Copyright 2026 Nikolai Kotchetkov.
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

package com.motorro.statemachine.books.app.state

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.statemachine.books.app.api.MainFlowHost
import com.motorro.statemachine.books.app.api.MainGesture
import com.motorro.statemachine.books.app.api.MainUiState
import com.motorro.statemachine.books.app.data.MainDataState
import com.motorro.statemachine.books.book.api.BookInput
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import javax.inject.Provider

/**
 * Main state factory
 */
internal interface MainFactory {
    fun bookList(data: MainDataState): CommonMachineState<MainGesture, MainUiState>
    fun book(data: MainDataState, input: BookInput): CommonMachineState<MainGesture, MainUiState>
    fun terminated(): CommonMachineState<MainGesture, MainUiState>

    @AssistedFactory
    interface Factory {
        fun create(host: MainFlowHost): Impl
    }

    class Impl @AssistedInject constructor(
        @Assisted private val host: MainFlowHost,
        private val createItemList: Provider<ItemListState.Factory>,
        private val createItem: Provider<ItemProxy.Factory>,
    ) : MainFactory {
        private val context = object : MainContext {
            override val factory: MainFactory = this@Impl
        }

        override fun bookList(data: MainDataState)  = createItemList.get().invoke(
            context,
            data
        )

        override fun book(data: MainDataState, input: BookInput) = createItem.get().invoke(
            context,
            data,
            input
        )

        override fun terminated() = object : CommonMachineState<MainGesture, MainUiState>() {
            override fun doStart() {
                host.onComplete(Unit)
            }
        }
    }
}
