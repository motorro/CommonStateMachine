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

package com.motorro.commonstatemachine.flow.data

/**
 * Common flow host.
 * Used as a common interface between master and child flow using proxy.
 * @param R - result type
 * @see com.motorro.commonstatemachine.ProxyMachineState
 * @see CommonFlowDataApi
 */
fun interface CommonFlowHost<in R> {
    /**
     * Completes common flow
     * @param result Optional result.
     */
    fun onComplete(result: R)
}