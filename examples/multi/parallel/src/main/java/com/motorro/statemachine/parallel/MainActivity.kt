package com.motorro.statemachine.parallel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.motorro.statemachine.androidcore.ui.theme.CommonStateMachineTheme
import com.motorro.statemachine.parallel.model.MainViewModel
import com.motorro.statemachine.parallel.model.data.ParallelGesture
import com.motorro.statemachine.timer.ui.TimerScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val model: MainViewModel = viewModel()
            val state by model.uiState.collectAsState()

            CommonStateMachineTheme {
                Scaffold { padding ->
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(padding),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(8.dp)
                                .border(1.dp, Color.Black)
                        ) {
                            TimerScreen(
                                modifier = Modifier.padding(padding),
                                title = "Top",
                                state = state.top
                            ) {
                                model.update(ParallelGesture.Top(it))
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(8.dp)
                                .border(1.dp, Color.Black)
                        ) {
                            TimerScreen(
                                modifier = Modifier.padding(padding),
                                title = "Bottom",
                                state = state.bottom
                            ) {
                                model.update(ParallelGesture.Bottom(it))
                            }
                        }
                    }
                }
            }
        }
    }
}

