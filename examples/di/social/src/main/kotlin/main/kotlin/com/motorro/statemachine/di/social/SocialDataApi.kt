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

package main.kotlin.com.motorro.statemachine.di.social

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.statemachine.di.api.AuthDataApi
import com.motorro.statemachine.di.api.AuthFlowHost
import com.motorro.statemachine.di.api.AuthGesture
import com.motorro.statemachine.di.api.AuthUiApi
import com.motorro.statemachine.di.api.AuthUiState
import jakarta.inject.Inject
import main.kotlin.com.motorro.statemachine.di.social.data.SocialGesture
import main.kotlin.com.motorro.statemachine.di.social.data.SocialUiState
import main.kotlin.com.motorro.statemachine.di.social.state.SocialFactory
import main.kotlin.com.motorro.statemachine.di.social.ui.SocialScreen

/**
 * Auth data API implementation for Social flow
 */
internal class SocialDataApi @Inject constructor(private val createFactory: SocialFactory.Factory) : AuthDataApi {
    /**
     * Initializes flow
     */
    override fun init(flowHost: AuthFlowHost, input: Unit?) = createFactory(flowHost).form()

    /**
     * Returns default UI state
     */
    override fun getDefaultUiState() = SocialUiState.Loading

    /**
     * Returns back gesture for this flow
     */
    override fun getBackGesture(): AuthGesture = SocialGesture.Back
}

/**
 * Auth UI API implementation for Social flow
 */
internal class SocialUiApi @Inject constructor() : AuthUiApi {
    @Composable
    override fun Screen(
        state: AuthUiState,
        onGesture: (AuthGesture) -> Unit,
        modifier: Modifier
    ) = SocialScreen(
        state = state as SocialUiState,
        onGesture = onGesture,
        modifier = modifier
    )
}