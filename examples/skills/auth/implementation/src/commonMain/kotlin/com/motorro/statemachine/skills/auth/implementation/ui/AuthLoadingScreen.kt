package com.motorro.statemachine.skills.auth.implementation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.motorro.statemachine.auth.appcore.ui.preview.SkillsPreviewComposition

@Composable
internal fun AuthLoadingScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Preview
@Composable
private fun AuthLoadingPreview() {
    SkillsPreviewComposition {
        AuthLoadingScreen(Modifier.fillMaxSize())
    }
}
