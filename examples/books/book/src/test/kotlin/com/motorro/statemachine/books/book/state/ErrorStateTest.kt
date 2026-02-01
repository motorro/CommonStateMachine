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
import com.motorro.statemachine.books.book.api.BookGesture
import com.motorro.statemachine.books.book.api.BookInput
import com.motorro.statemachine.books.book.api.BookUiState
import com.motorro.statemachine.books.book.data.BookDataState
import io.mockk.every
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class ErrorStateTest : BaseStateTest() {

    private lateinit var data: BookDataState
    private lateinit var error: Throwable
    private lateinit var state: ErrorState

    override fun doInit() {
        data = BookDataState(BookInput(BOOK_ID, BOOK_TITLE), Book)
        error = RuntimeException("Test error")
        state = ErrorState(context, data, error)
    }

    @Test
    fun displaysErrorOnStart() = test {
        state.start(stateMachine)

        verify {
            stateMachine.setUiState(withArg {
                val errorState = assertIs<BookUiState.Error>(it)
                assertEquals(error, errorState.error)
            })
        }
    }

    @Test
    fun terminatesOnBack() = test {
        every { factory.terminated() } returns nextState

        state.start(stateMachine)
        state.process(BookGesture.Back)

        verify {
            factory.terminated()
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun terminatesOnConfirm() = test {
        every { factory.terminated() } returns nextState

        state.start(stateMachine)
        state.process(BookGesture.Confirm)

        verify {
            factory.terminated()
            stateMachine.setMachineState(nextState)
        }
    }
}