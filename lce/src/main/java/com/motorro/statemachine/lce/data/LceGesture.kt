/*
 * Copyright 2022 Nikolai Kotchetkov.
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

package com.motorro.statemachine.lce.data

/**
 * Gestures for application flow
 */
sealed class LceGesture {
    /**
     * Item to load clicked
     * @property id Item ID to load
     */
    data class ItemClicked(val id: ItemId) : LceGesture()

    /**
     * Retry operation clicked
     */
    object Retry : LceGesture()

    /**
     * Backwards navigation gesture
     */
    object Back : LceGesture()

    /**
     * Terminates activity
     */
    object Exit : LceGesture()
}