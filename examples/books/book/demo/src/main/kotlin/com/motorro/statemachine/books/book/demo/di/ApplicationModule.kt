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

package com.motorro.statemachine.books.book.demo.di

import com.motorro.statemachine.books.book.demo.BOOK
import com.motorro.statemachine.books.domain.entity.Book
import com.motorro.statemachine.books.domain.entity.ListBook
import com.motorro.statemachine.books.domain.repository.BookRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

@Module
@InstallIn(SingletonComponent::class)
internal class ApplicationModule {
    @Provides
    @Singleton
    fun itemRepository(): BookRepository = object : BookRepository {
        override val books: Flow<ImmutableList<ListBook>>
            get() = throw NotImplementedError("Not implemented in this demo")

        override suspend fun getBook(id: Int): Book {
            delay(1.seconds)
            return BOOK
        }

        override suspend fun deleteBook(id: Int) {
            delay(1.seconds)
        }
    }
}