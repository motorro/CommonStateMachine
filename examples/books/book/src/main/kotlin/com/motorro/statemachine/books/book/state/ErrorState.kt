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
import com.motorro.statemachine.commoncore.log.Logger

internal class ErrorState(
    context: BookContext,
    private val data: BookDataState,
    private val error: Throwable
) : BaseBookState(context) {

    override fun doStart() {
        setUiState(
            BookUiState.Error(
                data.book?.title ?: data.input.title,
                error
            )
        )
    }

    override fun doProcess(gesture: BookGesture) {
        when(gesture) {
            BookGesture.Back, BookGesture.Confirm -> {
                Logger.d("Dismissed. Terminating...")
                setMachineState(factory.terminated())
            }
            else -> super.doProcess(gesture)
        }
    }
}