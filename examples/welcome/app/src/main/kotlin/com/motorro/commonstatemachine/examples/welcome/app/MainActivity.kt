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

package com.motorro.commonstatemachine.examples.welcome.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.motorro.commonstatemachine.examples.commoncore.ui.theme.CommonStateMachineTheme
import com.motorro.commonstatemachine.examples.welcome.app.view.WelcomeScreen
import io.github.aakira.napier.Napier


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CommonStateMachineTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    WelcomeScreen(
                        onTerminate = {
                            LaunchedEffect(
                                key1 = Unit,
                                block = { finish() }
                            )
                        }
                    )
                }
            }
        }
    }

    override fun finish() {
        Napier.d("Activity finished")
        super.finish()
    }
}
