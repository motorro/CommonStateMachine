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

package com.motorro.statemachine.books.book.api

import com.motorro.statemachine.books.domain.entity.Book

/**
 * Item sub-flow UI state
 */
sealed class BookUiState {
    /**
     * Loading
     */
    data class Loading(val title: String) : BookUiState()

    /**
     * Content
     */
    internal data class Content(val book: Book, val showDeleteConfirmation: Boolean) : BookUiState()

    /**
     * Error
     */
    internal data class Error(val title: String, val error: Throwable) : BookUiState()
}
