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

import com.motorro.statemachine.books.book.api.BookUiState
import com.motorro.statemachine.books.book.data.BookDataState
import com.motorro.statemachine.books.domain.repository.BookRepository
import com.motorro.statemachine.commoncore.log.Logger
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DeletingState(
    context: BookContext,
    private val data: BookDataState,
    private val repository: BookRepository
) : BaseBookState(context) {

    val item get() = checkNotNull(data.book) {
        "Illegal state: Item is null!"
    }

    override fun doStart() {
        setUiState(BookUiState.Loading(item.title))
        delete()
    }

    private fun delete() = stateScope.launch {
        Logger.d("Deleting item: ${item.id}")
        try {
            repository.deleteBook(item.id)
            Logger.d("Item ${item.id} deleted. Terminating...")
            setMachineState(factory.terminated())
        } catch (e: Throwable) {
            Logger.e(e, "Failed to delete item: ${item.id}")
            setMachineState(factory.error(data, e))
        }
    }

    class Factory @Inject constructor(private val repository: BookRepository) {
        operator fun invoke(context: BookContext, data: BookDataState) = DeletingState(
            context,
            data,
            repository
        )
    }
}