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

import com.motorro.statemachine.commoncore.log.Logger
import com.motorro.statemachine.di.api.data.Session
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import main.kotlin.com.motorro.statemachine.di.social.SocialConstants
import main.kotlin.com.motorro.statemachine.di.social.data.SocialGesture
import main.kotlin.com.motorro.statemachine.di.social.data.SocialUiState
import kotlin.time.Duration.Companion.seconds

/**
 * Emulates Social login
 */
internal class LoggingIn(context: SocialContext) : BaseSocialState(context) {
    override fun doStart() {
        setUiState(SocialUiState.Loading)
        Social()
    }

    private fun Social() = stateScope.launch {
        Logger.d("Logging in...")
        delay(2.seconds)
        setMachineState(factory.complete(Session.Active(
            accessToken = SocialConstants.ACCESS_TOKEN,
            refreshToken = SocialConstants.REFRESH_TOKEN
        )))
    }

    override fun doProcess(gesture: SocialGesture) {
        when(gesture) {
            SocialGesture.Back -> {
                Logger.d("Back pressed. Back to form...")
                setMachineState(factory.form())
            }
            else -> super.doProcess(gesture)
        }
    }
}