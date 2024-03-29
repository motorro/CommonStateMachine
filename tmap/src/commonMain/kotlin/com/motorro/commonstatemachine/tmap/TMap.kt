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

/**
 * Typed key
 */
interface TKey<E : Any>

/**
 * Element bound with a key
 */
interface TElement<E: Any, K: TKey<E>> {
    val key: K
    val value: E
}

/**
 * Stores typed key-value pairs
 */
interface TMap : Collection<TElement<*, *>> {

    operator fun <E: Any, K: TKey<E>> get(key: K): E?

    operator fun <E: Any, K: TKey<E>> plus(value: TElement<E, K>): TMap

    fun minusKey(key: TKey<*>): TMap

    companion object {
        fun empty(): TMap = EmptyTMap
        fun of(vararg element: TElement<*, *>): TMap = of(element.toList())
        fun of(elements: Iterable<TElement<*, *>>): TMap = TMapImpl(elements.associateBy{ it.key })
    }
}

/**
 * Typed [to] (to-typed)
 * Binds a key with value
 * @receiver Key
 * @param value Value
 */
infix fun <E: Any, K: TKey<E>> K.tot(value: E): TElement<E, K> = TElementImpl(this, value)

/**
 * Element implementation
 */
private data class TElementImpl<E: Any, K: TKey<E>>(
    override val key: K,
    override val value: E
) : TElement<E, K>

/**
 * Empty map
 */
private data object EmptyTMap : TMap {

    private val iterator = object : Iterator<TElement<*, *>> {
        override fun hasNext(): Boolean = false
        override fun next(): TElement<*, *> = throw NoSuchElementException("No next element. Empty collection")
    }

    override fun <E : Any, K : TKey<E>> get(key: K): E? = null
    override fun <E : Any, K : TKey<E>> plus(value: TElement<E, K>): TMap = TMapImpl(linkedMapOf(value.key to value))
    override fun minusKey(key: TKey<*>)= this

    override val size: Int = 0
    override fun contains(element: TElement<*, *>) = false
    override fun containsAll(elements: Collection<TElement<*, *>>) = false
    override fun isEmpty() = true
    override fun iterator() = iterator
}

/**
 * A map with contents
 */
private class TMapImpl(private val contents: Map<TKey<*>, TElement<*, *>>) : TMap {

    @Suppress("UNCHECKED_CAST")
    override fun <E : Any, K : TKey<E>> get(key: K): E? = contents[key]?.value?.let { it as E }
    override fun <E : Any, K : TKey<E>> plus(value: TElement<E, K>): TMap = TMapImpl(contents.plus(value.key to value))
    override fun minusKey(key: TKey<*>) = when {
        contents.containsKey(key) && 1 == contents.size -> EmptyTMap
        contents.containsKey(key) -> TMapImpl(contents.minus(key))
        else -> this
    }

    override val size: Int get() = contents.size
    override fun contains(element: TElement<*, *>) = contents.containsValue(element)
    override fun containsAll(elements: Collection<TElement<*, *>>) = elements.all { contains(it) }
    override fun isEmpty(): Boolean = contents.isEmpty()

    override fun iterator() = object : Iterator<TElement<*, *>> {
        private val parent = contents.iterator()
        override fun hasNext(): Boolean = parent.hasNext()
        override fun next(): TElement<*, *> = parent.next().value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TMapImpl) return false

        if (contents != other.contents) return false

        return true
    }

    override fun hashCode(): Int {
        return contents.hashCode()
    }

    override fun toString(): String {
        return "TypedKeyMap(${contents.values})"
    }
}
