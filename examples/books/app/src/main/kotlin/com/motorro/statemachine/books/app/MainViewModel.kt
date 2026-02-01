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

package com.motorro.statemachine.books.app

import com.motorro.commonstatemachine.flow.viewmodel.CommonFlowViewModel
import com.motorro.statemachine.books.app.api.MainApi
import com.motorro.statemachine.books.app.api.MainGesture
import com.motorro.statemachine.books.app.api.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(mainApi: MainApi) : CommonFlowViewModel<MainGesture, MainUiState, Unit, Unit>(
    api = mainApi,
    init = Unit
)