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

package com.motorro.statemachine.books.book

import com.motorro.statemachine.books.domain.entity.Book
import kotlinx.collections.immutable.persistentListOf

internal const val BOOK_ID = 10
internal const val BOOK_TITLE = "Book title"

internal val Book = Book(
    id = BOOK_ID,
    title = BOOK_TITLE,
    authors = persistentListOf("Author 1", "Author 2"),
    content = "Book content"
)