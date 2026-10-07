package com.motorro.commonstatemachine.examples.skills.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.motorro.commonstatemachine.examples.auth.appcore.ui.theme.SkillsTheme
import com.motorro.commonstatemachine.examples.skills.app.ui.MainScreen

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

