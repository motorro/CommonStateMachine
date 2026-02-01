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

package com.motorro.statemachine.books.app.api

import androidx.compose.runtime.Immutable
import com.motorro.statemachine.books.book.api.BookUiState
import com.motorro.statemachine.books.domain.entity.ListBook
import kotlinx.collections.immutable.ImmutableList

/**
 * Main flow UI state
 */
sealed class MainUiState {
    /**
     * Main flow
     */
    sealed class List : MainUiState() {
        /**
         * Main loader
         */
        internal data object Loading : List()

        /**
         * Main list
         */
        @Immutable
        internal data class Master(val items: ImmutableList<ListBook>) : List()
    }

    /**
     * Book detail
     */
    internal data class Book(val child: BookUiState) : MainUiState()
}