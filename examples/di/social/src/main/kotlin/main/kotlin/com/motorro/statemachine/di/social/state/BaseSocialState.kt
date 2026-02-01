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

package main.kotlin.com.motorro.statemachine.di.social.state

import com.motorro.commonstatemachine.coroutines.CoroutineState
import com.motorro.statemachine.commoncore.log.Logger
import com.motorro.statemachine.di.api.AuthGesture
import com.motorro.statemachine.di.api.AuthUiState
import main.kotlin.com.motorro.statemachine.di.social.data.SocialGesture

internal abstract class BaseSocialState(context: SocialContext) : CoroutineState<AuthGesture, AuthUiState>(), SocialContext by context {

    final override fun doProcess(gesture: AuthGesture) {
        when (gesture) {
            is SocialGesture -> doProcess(gesture)
            else -> Logger.w("Not a `SocialGesture`: $gesture")
        }
    }

    protected open fun doProcess(gesture: SocialGesture) {
        Logger.w("Gesture not handled: $gesture")
    }
}
