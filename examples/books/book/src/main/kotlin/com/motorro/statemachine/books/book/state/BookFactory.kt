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

package com.motorro.statemachine.books.book.state

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.statemachine.books.book.api.BookGesture
import com.motorro.statemachine.books.book.api.BookInput
import com.motorro.statemachine.books.book.api.BookUiState
import com.motorro.statemachine.books.book.api.ItemFlowHost
import com.motorro.statemachine.books.book.data.BookDataState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import javax.inject.Provider

internal interface BookFactory {
    fun loading(input: BookInput): CommonMachineState<BookGesture, BookUiState>
    fun content(data: BookDataState): CommonMachineState<BookGesture, BookUiState>
    fun deleteConfirmation(data: BookDataState): CommonMachineState<BookGesture, BookUiState>
    fun error(dataState: BookDataState, error: Throwable): CommonMachineState<BookGesture, BookUiState>
    fun deleting(data: BookDataState): CommonMachineState<BookGesture, BookUiState>
    fun terminated(): CommonMachineState<BookGesture, BookUiState>

    @AssistedFactory
    interface Factory {
        fun create(host: ItemFlowHost): Impl
    }

    class Impl @AssistedInject constructor(
        @Assisted private val host: ItemFlowHost,
        private val createLoadingState: Provider<LoadingState.Factory>,
        private val createDeletingState: Provider<DeletingState.Factory>
    ) : BookFactory {

        private val context = object : BookContext {
            override val factory: BookFactory = this@Impl
        }

        override fun loading(input: BookInput): CommonMachineState<BookGesture, BookUiState> = createLoadingState.get().invoke(
            context,
            BookDataState(input)
        )

        override fun content(data: BookDataState) = ContentState(
            context,
            data
        )

        override fun deleteConfirmation(data: BookDataState) = DeleteConfirmationState(
            context,
            data
        )

        override fun error(dataState: BookDataState, error: Throwable) = ErrorState(
            context,
            dataState,
            error
        )

        override fun deleting(data: BookDataState) = createDeletingState.get().invoke(
            context,
            data
        )

        override fun terminated() = object : CommonMachineState<BookGesture, BookUiState>() {
            override fun doStart() {
                host.onComplete(Unit)
            }
        }
    }
}