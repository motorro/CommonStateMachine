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

import com.motorro.statemachine.books.app.BOOK_1
import com.motorro.statemachine.books.app.BOOK_2
import com.motorro.statemachine.books.app.api.MainGesture
import com.motorro.statemachine.books.app.api.MainUiState
import com.motorro.statemachine.books.app.data.MainDataState
import com.motorro.statemachine.books.book.api.BookInput
import com.motorro.statemachine.books.domain.entity.ListBook
import com.motorro.statemachine.books.domain.repository.BookRepository
import io.mockk.Ordering
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.yield
import org.junit.Test
import kotlin.test.assertEquals

internal class ItemListStateTest : BaseStateTest() {
    private lateinit var items: MutableStateFlow<ImmutableList<ListBook>>

    override fun doInit() {
        items = MutableStateFlow(persistentListOf(BOOK_1))
    }

    private fun createState(data: MainDataState = MainDataState()): BaseMainState {
        val repository = mockk<BookRepository> {
            every { this@mockk.books } returns this@ItemListStateTest.items
        }
        return ItemListState(context, data, repository)
    }

    @Test
    fun startsWithLoadingWhenDataIsEmpty() = test {
        val state = createState()

        state.start(stateMachine)

        coVerify(ordering = Ordering.ORDERED) {
            stateMachine.setUiState(MainUiState.Loading)
            stateMachine.setUiState(MainUiState.Master(persistentListOf(BOOK_1)))
        }
    }

    @Test
    fun startsWithCachedItemsWhenDataIsNotEmpty() = test {
        val state = createState(MainDataState(items = persistentListOf(BOOK_2)))

        state.start(stateMachine)

        coVerify(ordering = Ordering.ORDERED) {
            stateMachine.setUiState(MainUiState.Master(persistentListOf(BOOK_2)))
            stateMachine.setUiState(MainUiState.Master(persistentListOf(BOOK_1)))
        }
    }

    @Test
    fun updatesWithRepository() = test {
        val state = createState()

        state.start(stateMachine)
        items.emit(persistentListOf(BOOK_2))

        coVerify(ordering = Ordering.ORDERED) {
            stateMachine.setUiState(MainUiState.Loading)
            stateMachine.setUiState(MainUiState.Master(persistentListOf(BOOK_1)))
            stateMachine.setUiState(MainUiState.Master(persistentListOf(BOOK_2)))
        }
    }

    @Test
    fun selectsItem() = test {
        every { factory.book(any(), any()) } returns nextState
        val state = createState()

        state.start(stateMachine)
        yield()
        state.process(MainGesture.BookSelected(BOOK_1.id))

        coVerify {
            factory.book(
                data = withArg {
                    assertEquals(persistentListOf(BOOK_1), it.items)
                },
                input = eq(BookInput(BOOK_1.id, BOOK_1.title))
            )
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun doesNothingOnInvalidItem() = test {
        every { factory.book(any(), any()) } returns nextState
        val state = createState()

        state.start(stateMachine)
        yield()
        state.process(MainGesture.BookSelected(100500))

        coVerify(exactly = 0) {
            factory.book(any(), any())
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun terminatesOnBack() {
        every { factory.terminated() } returns nextState
        val state = createState()

        state.start(stateMachine)
        state.process(MainGesture.Back)

        coVerify {
            factory.terminated()
            stateMachine.setMachineState(nextState)
        }
    }
}