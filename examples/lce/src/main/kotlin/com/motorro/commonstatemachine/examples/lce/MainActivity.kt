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

package com.motorro.commonstatemachine.examples.lce

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import com.motorro.commonstatemachine.examples.commoncore.ui.theme.CommonStateMachineTheme
import com.motorro.commonstatemachine.examples.lce.ui.LceScreen
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Napier.base(DebugAntilog())

        setContent {
            CommonStateMachineTheme {
                LceScreen(
                    onExit = {
                        LaunchedEffect(key1 = Unit, block = { finish() })
                    }
                )
            }
        }
    }
}
