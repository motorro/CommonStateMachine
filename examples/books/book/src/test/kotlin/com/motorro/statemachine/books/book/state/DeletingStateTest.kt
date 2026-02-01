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

import com.motorro.statemachine.books.book.BOOK_ID
import com.motorro.statemachine.books.book.BOOK_TITLE
import com.motorro.statemachine.books.book.Book
import com.motorro.statemachine.books.book.api.BookInput
import com.motorro.statemachine.books.book.api.BookUiState
import com.motorro.statemachine.books.book.data.BookDataState
import com.motorro.statemachine.books.domain.repository.BookRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

internal class DeletingStateTest : BaseStateTest() {

    private lateinit var data: BookDataState
    private lateinit var repository: BookRepository
    private lateinit var state: DeletingState

    override fun doInit() {
        data = BookDataState(BookInput(BOOK_ID, BOOK_TITLE), Book)
        repository = mockk()
        state = DeletingState(context, data, repository)
    }

    @Test
    fun deletesItemAndTerminates() = test {
        coEvery { repository.deleteBook(any()) } returns Unit
        every { factory.terminated() } returns nextState

        state.start(stateMachine)

        coVerify {
            stateMachine.setUiState(BookUiState.Loading(BOOK_TITLE))
            repository.deleteBook(Book.id)
            factory.terminated()
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun advancesToErrorIfFails() = test {
        val error = RuntimeException("Test error")
        coEvery { repository.deleteBook(any()) } throws error
        every { factory.error(any(), any()) } returns nextState

        state.start(stateMachine)

        coVerify {
            stateMachine.setUiState(BookUiState.Loading(BOOK_TITLE))
            repository.deleteBook(Book.id)
            factory.error(any(), error)
            stateMachine.setMachineState(nextState)
        }
    }
}