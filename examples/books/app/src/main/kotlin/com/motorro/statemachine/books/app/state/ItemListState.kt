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

import com.motorro.statemachine.books.app.api.MainGesture
import com.motorro.statemachine.books.app.api.MainUiState
import com.motorro.statemachine.books.app.data.MainDataState
import com.motorro.statemachine.books.book.api.BookInput
import com.motorro.statemachine.books.domain.entity.ListBook
import com.motorro.statemachine.books.domain.repository.BookRepository
import com.motorro.statemachine.commoncore.log.Logger
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.properties.Delegates

internal class ItemListState(
    context: MainContext,
    data: MainDataState,
    private val repository: BookRepository
) : BaseMainState(context) {

    private var data: MainDataState by Delegates.observable(data) { _, _, newValue ->
        render(newValue.items)
    }

    override fun doStart() {
        render(data.items)
        subscribeSession()
    }

    private fun subscribeSession() {
        repository.books
            .onEach { data = data.copy(items = it) }
            .launchIn(stateScope)
    }

    override fun doProcess(gesture: MainGesture) {
        when (gesture) {
            MainGesture.Back -> {
                Logger.d("Back. Terminating...")
                setMachineState(factory.terminated())
            }
            is MainGesture.BookSelected -> {
                val selected = data.items?.firstOrNull { it.id == gesture.id } ?: return
                Logger.d("Item selected: ${selected.id}")
                setMachineState(factory.book(
                    data = data,
                    input = BookInput(selected.id, selected.title)
                ))
            }
            else -> super.doProcess(gesture)
        }
    }

    private fun render(items: ImmutableList<ListBook>?) {
        val uiState = when {
            null != items -> MainUiState.List.Master(items)
            else -> MainUiState.List.Loading
        }
        setUiState(uiState)
    }

    class Factory @Inject constructor(private val repository: BookRepository) {
        fun invoke(context: MainContext, data: MainDataState) = ItemListState(
            context,
            data,
            repository
        )
    }
}