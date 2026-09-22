package com.motorro.statemachine.skills.auth.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.motorro.commonstatemachine.flow.viewmodel.CommonFlowComposition
import com.motorro.statemachine.auth.appcore.ui.design.SkillsAppBar
import com.motorro.statemachine.auth.appcore.ui.theme.SkillsTheme
import com.motorro.statemachine.skills.auth.api.AuthUiApi
import io.github.aakira.napier.Napier
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SkillsTheme {
                MainScreen {
                    finish()
                }
            }
        }
    }
}

@Composable
fun MainScreen(onComplete: () -> Unit) {
    val viewModel: MainViewModel = koinViewModel()
    val uiApi: AuthUiApi = koinInject()

    CommonFlowComposition(
        viewModel = viewModel,
        navigationBackHandler = { enabled, onBack ->
            BackHandler(enabled, onBack)
        },
        content = { state, onGesture ->
            Scaffold(
                topBar = {
                    SkillsAppBar(
                        title = stringResource(R.string.app_name),
                        modifier = Modifier.fillMaxWidth(),
                        topLevel = true,
                    )
                }
            ) { paddingValues ->
                uiApi.Screen(
                    state,
                    onGesture,
                    Modifier.padding(paddingValues)
                )
            }
        },
        finish = {
            Napier.i { "Finished with: $it" }
            onComplete()
        }
    )
}

