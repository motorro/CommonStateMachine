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

/**
 * Machine key
 * Identifies the machine in multi-machine bundles
 * @param G Gesture type
 * @param U UI-state type
 * @param tag An extra string to distinguish one key from the other
 */
abstract class MachineKey<G: Any, U: Any>(val tag: String? = null) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as MachineKey<*, *>

        if (tag != other.tag) return false

        return true
    }

    override fun hashCode(): Int {
        return tag?.hashCode() ?: 0
    }
}
