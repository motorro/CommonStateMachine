package com.motorro.statemachine.auth.appcore.ui.design

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.motorro.statemachine.auth.appcore.ui.preview.SkillsPreviewComposition
import com.motorro.statemachine.skills.appcore.Res
import com.motorro.statemachine.skills.appcore.btn_back
import org.jetbrains.compose.resources.stringResource

/**
 * Common top bar sample
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SkillsAppBar(
    title: String,
    modifier: Modifier = Modifier,
    topLevel: Boolean = true,
    actions: @Composable RowScope.() -> Unit = { },
    onBack: () -> Unit = { }
) {
    NavigationBackHandler(
        state = rememberNavigationEventState(
            currentInfo = NavigationEventInfo.None
        ),
        isBackEnabled = topLevel.not(),
        onBackCompleted = onBack
    )

    TopAppBar(
        title = { Text(title) },
        modifier = modifier,
        navigationIcon = {
            if (topLevel.not()) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(Res.string.btn_back)
                    )
                }
            }
        },
        actions = actions
    )
}


private data class AppBarData(val title: String, val topLevel: Boolean = false, val actions: List<ImageVector> = emptyList())

private class AppBarDataProvider : PreviewParameterProvider<AppBarData> {
    override val values: Sequence<AppBarData> = sequenceOf(
        AppBarData("Top Level", topLevel = true),
        AppBarData("Top Level Actions", topLevel = true, listOf(
            Icons.Rounded.AddCircle,
            Icons.Rounded.Call
        )),
        AppBarData("Inner", topLevel = false),
        AppBarData("Inner Actions", topLevel = false, listOf(
            Icons.Rounded.AddCircle,
            Icons.Rounded.Call
        ))
    )
}

@Preview
@Composable
private fun SkillsScreenPreview(@PreviewParameter(AppBarDataProvider::class) data: AppBarData?) {
    if (null == data) return
    SkillsPreviewComposition {
        SkillsAppBar(
            title = data.title,
            topLevel = data.topLevel,
            actions = {
                data.actions.forEach {
                    IconButton(onClick = {}) {
                        Icon(it, null)
                    }
                }
            }
        )
    }
}
