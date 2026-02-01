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

import com.motorro.statemachine.books.book.api.BookGesture
import com.motorro.statemachine.books.book.api.BookUiState
import com.motorro.statemachine.books.book.data.BookDataState
import com.motorro.statemachine.books.domain.repository.BookRepository
import com.motorro.statemachine.commoncore.log.Logger
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class LoadingState(
    context: BookContext,
    private val data: BookDataState,
    private val repository: BookRepository
) : BaseBookState(context) {

    override fun doStart() {
        setUiState(BookUiState.Loading(data.input.title))
        load()
    }

    private fun load() = stateScope.launch {
        val id = data.input.id
        Logger.d("Loading item: $id")
        try {
            val item = repository.getBook(id)
            Logger.d("Item loaded: $item")
            setMachineState(factory.content(data.copy(book = item)))
        } catch (e: Throwable) {
            ensureActive()
            Logger.e(e, "Failed to load item: $id")
            setMachineState(factory.error(data, e))
        }
    }

    override fun doProcess(gesture: BookGesture) {
        when (gesture) {
            BookGesture.Back -> {
                Logger.w("Back gesture. Aborting load...")
                setMachineState(factory.terminated())
            }
            else -> super.doProcess(gesture)
        }
    }

    class Factory @Inject constructor(private val repository: BookRepository) {
        operator fun invoke(context: BookContext, dataState: BookDataState) = LoadingState(
            context,
            dataState,
            repository
        )
    }
}