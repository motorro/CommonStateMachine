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

package main.kotlin.com.motorro.statemachine.di.social.di

import com.motorro.statemachine.di.api.AuthDataApi
import com.motorro.statemachine.di.api.AuthUiApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import main.kotlin.com.motorro.statemachine.di.social.SocialDataApi
import main.kotlin.com.motorro.statemachine.di.social.SocialUiApi

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class SocialFlowDataModule {
    @Binds
    abstract fun bindSocialDataApi(impl: SocialDataApi): AuthDataApi
}

@Module
@InstallIn(ActivityComponent::class)
internal abstract class SocialFlowUiModule {
    @Binds
    abstract fun bindSocialUiApi(impl: SocialUiApi): AuthUiApi
}