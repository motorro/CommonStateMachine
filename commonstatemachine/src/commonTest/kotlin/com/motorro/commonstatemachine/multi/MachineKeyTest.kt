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

package com.motorro.commonstatemachine.multi

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class MachineKeyTest {
    private class IntKey(tag: String): MachineKey<Int, Int>(tag)
    private class StringKey(tag: String): MachineKey<String, String>(tag)

    @Test
    fun twoIntKeysWithSameTagAreEqual() {
        val i1 = IntKey("1")
        val i2 = IntKey("1")
        assertEquals(i1, i2)
    }

    @Test
    fun twoIntWithDifferentTagsAreNotEqual() {
        val i1 = IntKey("1")
        val i2 = IntKey("2")
        assertNotEquals(i1, i2)
    }

    @Test
    fun intAndStringKeysWithSameTagAreNotEqual() {
        val i = IntKey("1")
        val s = StringKey("1")
        assertNotEquals<Any>(i, s)
    }
}