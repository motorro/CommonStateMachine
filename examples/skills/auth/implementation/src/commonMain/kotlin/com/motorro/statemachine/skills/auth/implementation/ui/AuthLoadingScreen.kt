package com.motorro.statemachine.skills.auth.implementation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.motorro.statemachine.auth.appcore.ui.design.SkillsLoading
import com.motorro.statemachine.auth.appcore.ui.preview.SkillsPreviewComposition

@Composable
internal fun AuthLoadingScreen(modifier: Modifier = Modifier) {
    SkillsLoading(modifier)
}

@Preview
@Composable
private fun AuthLoadingPreview() {
    SkillsPreviewComposition {
        AuthLoadingScreen(Modifier.fillMaxSize())
    }
}
