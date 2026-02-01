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

package com.motorro.statemachine.books.app

import com.motorro.statemachine.books.domain.entity.ListBook
import kotlinx.collections.immutable.persistentListOf

val BOOK_1 = ListBook(
    id = 10,
    title = "Book 1",
    authors = persistentListOf("Author 1", "Author 2")
)

val BOOK_2 = ListBook(
    id = 20,
    title = "Book 2",
    authors = persistentListOf("Author 3", "Author 4")
)