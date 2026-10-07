package com.motorro.commonstatemachine.examples.skills.auth.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.motorro.commonstatemachine.examples.auth.appcore.ui.design.SkillsAppBar
import com.motorro.commonstatemachine.examples.auth.appcore.ui.theme.Dimensions
import com.motorro.commonstatemachine.examples.auth.appcore.ui.theme.SkillsTheme
import com.motorro.commonstatemachine.examples.skills.auth.api.AuthResult
import com.motorro.commonstatemachine.examples.skills.auth.api.AuthUiApi
import com.motorro.commonstatemachine.flow.viewmodel.CommonFlowComposition
import io.github.aakira.napier.Napier
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val (result, setResult) = remember {
                mutableStateOf<AuthResult?>(null)
            }

            SkillsTheme {
                Scaffold(
                    topBar = {
                        SkillsAppBar(
                            title = stringResource(R.string.app_name),
                            modifier = Modifier.fillMaxWidth(),
                            topLevel = true
                        )
                    }
                ) { paddingValues ->
                    if (null == result) {
                        MainScreen(
                            modifier = Modifier.padding(paddingValues),
                            onComplete = setResult
                        )
                    } else {
                        ResultScreen(result) {
                            finish()
                        }
                    }
                }
            }
        }
    }
}

/**
 * Feature composition
 */
@Composable
private fun MainScreen(modifier: Modifier = Modifier, onComplete: (AuthResult) -> Unit) {
    val viewModel: MainViewModel = koinViewModel()
    val uiApi: AuthUiApi = koinInject()

    CommonFlowComposition(
        viewModel = viewModel,
        navigationBackHandler = { enabled, onBack ->
            BackHandler(enabled, onBack)
        },
        content = { state, onGesture ->
            uiApi.Screen(
                state = state,
                onGesture = onGesture,
                modifier = modifier
            )
        },
        finish = {
            Napier.i { "Finished with: $it" }
            onComplete(it)
        }
    )
}

/**
 * Describes the flow result
 */
@Composable
private fun ResultScreen(result: AuthResult, onComplete: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Dimensions.medium, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            style = MaterialTheme.typography.bodyLarge,
            text = stringResource(R.string.result_authenticated, result.authenticated)
        )
        Button(onClick = onComplete) {
            Text(text = stringResource(R.string.btn_exit))
        }
    }
}

