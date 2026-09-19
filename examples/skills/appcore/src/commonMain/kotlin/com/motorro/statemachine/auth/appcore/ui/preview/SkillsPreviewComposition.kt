package com.motorro.statemachine.auth.appcore.ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.navigationevent.NavigationEventDispatcher
import androidx.navigationevent.NavigationEventDispatcherOwner
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner
import com.motorro.statemachine.auth.appcore.ui.theme.SkillsTheme

/**
 * App preview with all the required locals and theming
 */
@Composable
fun SkillsPreviewComposition(content: @Composable () -> Unit) {

    val navEventDispatcherOwner = remember {
        object : NavigationEventDispatcherOwner {
            override val navigationEventDispatcher: NavigationEventDispatcher = NavigationEventDispatcher()
        }
    }

    SkillsTheme {
        CompositionLocalProvider(LocalNavigationEventDispatcherOwner provides navEventDispatcherOwner) {
            content()
        }
    }
}