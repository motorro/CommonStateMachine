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

package com.motorro.statemachine.books.app.di

import com.motorro.statemachine.books.app.repository.BookRepositoryImpl
import com.motorro.statemachine.books.domain.entity.Book
import com.motorro.statemachine.books.domain.repository.BookRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.collections.immutable.persistentListOf
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class ApplicationModule {
    @Provides
    @Singleton
    fun itemRepository(): BookRepository = BookRepositoryImpl(
        listOf(
            Book(
                id = 1,
                title = "The Hobbit",
                authors = persistentListOf("J.R.R. Tolkien"),
                content = "A reluctant hobbit, Bilbo Baggins, sets out to the Lonely Mountain with a spirited group of dwarves to reclaim their mountain home, and a treasure within, from the dragon Smaug."
            ),
            Book(
                id = 2,
                title = "The Lord of the Rings",
                authors = persistentListOf("J.R.R. Tolkien"),
                content = "Frodo Baggins, a hobbit, and his companions set out on a quest to destroy the powerful One Ring and save Middle-earth from the Dark Lord Sauron."
            ),
            Book(
                id = 3,
                title = "Pride and Prejudice",
                authors = persistentListOf("Jane Austen"),
                content = "Follows the turbulent relationship between Elizabeth Bennet, the daughter of a country gentleman, and Fitzwilliam Darcy, a rich aristocratic landowner."
            ),
            Book(
                id = 4,
                title = "To Kill a Mockingbird",
                authors = persistentListOf("Harper Lee"),
                content = "The story of a young girl, Scout Finch, her brother, Jem, and their father, Atticus, a lawyer who defends a black man falsely accused of raping a white woman in the 1930s Alabama."
            ),
            Book(
                id = 5,
                title = "1984",
                authors = persistentListOf("George Orwell"),
                content = "A dystopian novel set in Airstrip One (formerly Great Britain), a province of the superstate Oceania in a world of perpetual war, omnipresent government surveillance, and public manipulation."
            )
        )
    )
}