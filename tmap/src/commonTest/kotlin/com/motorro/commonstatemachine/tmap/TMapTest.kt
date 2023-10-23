/*
 * Copyright 2023 Nikolai Kotchetkov.
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

package com.motorro.commonstatemachine.tmap

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class TMapTest {
    private data object StringKey: TKey<String>
    private data object IntKey: TKey<Int>
    private data object BoolKey: TKey<Boolean>

    private data class NamedStringKey(val name: String): TKey<String>
    private val a = NamedStringKey("a")
    private val b = NamedStringKey("b")
    private val c = NamedStringKey("c")
    private val d = NamedStringKey("d")

    @Test
    fun createsEmptyMap() {
        val empty = TMap.empty()

        assertEquals(0, empty.size)
        assertFalse(empty.iterator().hasNext())
    }

    @Test
    fun emptyAddsElements() {
        val empty = TMap.empty()
        val plusElement = empty.plus(StringKey.tot("String"))

        val fromContent: String? = plusElement[StringKey]
        assertNotNull(fromContent)
        assertEquals("String", fromContent)
    }

    @Test
    fun emptyRemovesElements() {
        val empty = TMap.empty()
        val minusElement = empty.minusKey(StringKey)

        assertEquals(0, minusElement.size)
        assertFalse(minusElement.iterator().hasNext())
    }

    @Test
    fun createsMapWithContent() {
        val withContent = TMap.of(
            StringKey.tot("String"),
            IntKey.tot(1),
            BoolKey.tot(true)
        )

        assertEquals(3, withContent.size)
        assertEquals(
            listOf(
                StringKey.tot("String"),
                IntKey.tot(1),
                BoolKey.tot(true)
            ),
            withContent.toList()
        )

        val stringFromContent: String? = withContent[StringKey]
        assertNotNull(stringFromContent)
        assertEquals("String", stringFromContent)

        val intFromContent: Int? = withContent[IntKey]
        assertNotNull(intFromContent)
        assertEquals(1, intFromContent)

        val boolFromContent: Boolean? = withContent[BoolKey]
        assertNotNull(boolFromContent)
        assertEquals(true, boolFromContent)
    }

    @Test
    fun createsMapWithNamedContent() {
        val withContent = TMap.of(
            a.tot("A"),
            b.tot("B"),
            c.tot("C")
        )

        assertEquals(3, withContent.size)
        assertEquals(
            listOf(
                a.tot("A"),
                b.tot("B"),
                c.tot("C")
            ),
            withContent.toList()
        )

        val aFromContent: String? = withContent[a]
        assertNotNull(aFromContent)
        assertEquals("A", aFromContent)

        val bFromContent: String? = withContent[b]
        assertNotNull(bFromContent)
        assertEquals("B", bFromContent)

        val cFromContent: String? = withContent[c]
        assertNotNull(cFromContent)
        assertEquals("C", cFromContent)

        val dFromContent: String? = withContent[d]
        assertNull(dFromContent)
    }

    @Test
    fun withContentRemovesKey() {
        val withContent = TMap.of(
            StringKey.tot("String"),
            IntKey.tot(1),
            BoolKey.tot(true)
        )

        val withoutString = withContent.minusKey(StringKey)
        assertEquals(2, withoutString.size)
        assertEquals(
            listOf(
                IntKey.tot(1),
                BoolKey.tot(true)
            ),
            withoutString.toList()
        )

        val empty = withoutString.minusKey(IntKey).minusKey(BoolKey)
        assertEquals(0, empty.size)
        assertFalse(empty.iterator().hasNext())
    }

    @Test
    fun withContentRemovesAbsentKey() {
        val withContent = TMap.of(
            StringKey.tot("String"),
            IntKey.tot(1)
        )

        val withoutBool = withContent.minusKey(BoolKey)
        assertEquals(2, withoutBool.size)
        assertEquals(
            listOf(
                StringKey.tot("String"),
                IntKey.tot(1)
            ),
            withoutBool.toList()
        )
    }
}