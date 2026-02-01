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

package com.motorro.statemachine.books.book.demo

import com.motorro.commonstatemachine.flow.viewmodel.CommonFlowViewModel
import com.motorro.statemachine.books.book.api.BookApi
import com.motorro.statemachine.books.book.api.BookGesture
import com.motorro.statemachine.books.book.api.BookInput
import com.motorro.statemachine.books.book.api.BookUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = MainViewModel.Factory::class)
class MainViewModel @AssistedInject internal constructor(
    @Assisted input: BookInput,
    bookApi: BookApi
) : CommonFlowViewModel<BookGesture, BookUiState, BookInput, Unit>(
    api = bookApi,
    init = input
) {
    @AssistedFactory
    interface Factory {
        fun create(input: BookInput): MainViewModel
    }
}