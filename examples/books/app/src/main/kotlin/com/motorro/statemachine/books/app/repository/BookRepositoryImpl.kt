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

package com.motorro.statemachine.books.app.repository

import com.motorro.statemachine.books.domain.entity.Book
import com.motorro.statemachine.books.domain.entity.ListBook
import com.motorro.statemachine.books.domain.repository.BookRepository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlin.time.Duration.Companion.seconds

class BookRepositoryImpl(books: List<Book>) : BookRepository {

    private val data = MutableStateFlow(books.associateBy { it.id })

    override val books: Flow<ImmutableList<ListBook>> = flow {
        delay(DELAY)
        emitAll(data.map { it.values.map { ListBook(it.id, it.title, it.authors) }.toImmutableList() })
    }

    override suspend fun getBook(id: Int): Book {
        delay(DELAY)
        return data.first().getValue(id)
    }

    override suspend fun deleteBook(id: Int) {
        delay(DELAY)
        data.update {
            it - id
        }
    }

    private companion object {
        val DELAY = 1.seconds
    }
}