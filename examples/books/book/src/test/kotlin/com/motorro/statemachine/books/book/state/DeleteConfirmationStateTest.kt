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

internal class DeleteConfirmationStateTest : BaseStateTest() {

    private lateinit var data: BookDataState
    private lateinit var state: DeleteConfirmationState

    override fun doInit() {
        data = BookDataState(BookInput(BOOK_ID, BOOK_TITLE), Book)
        state = DeleteConfirmationState(context, data)
    }

    @Test
    fun displaysConfirmationOnStart() = test {
        state.start(stateMachine)

        verify {
            stateMachine.setUiState(withArg {
                val content = assertIs<BookUiState.Content>(it)
                assertEquals(Book, content.book)
                assertEquals(true, content.showDeleteConfirmation)
            })
        }
    }

    @Test
    fun returnsToContentOnBack() = test {
        every { factory.content(any()) } returns nextState

        state.start(stateMachine)
        state.process(BookGesture.Back)

        verify {
            factory.content(data)
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun returnsToContentOnCancel() = test {
        every { factory.content(any()) } returns nextState

        state.start(stateMachine)
        state.process(BookGesture.Cancel)

        verify {
            factory.content(data)
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun proceedsToDeletingOnConfirm() = test {
        every { factory.deleting(any()) } returns nextState

        state.start(stateMachine)
        state.process(BookGesture.Confirm)

        verify {
            factory.deleting(data)
            stateMachine.setMachineState(nextState)
        }
    }
}