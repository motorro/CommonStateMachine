package com.motorro.statemachine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.motorro.statemachine.registration.view.RegistrationScreen
import com.motorro.statemachine.ui.theme.StateMachineTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            StateMachineTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    RegistrationScreen(
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
        Timber.d("Activity finished")
        super.finish()
    }
}
