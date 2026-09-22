package com.motorro.statemachine.skills.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.motorro.statemachine.auth.appcore.ui.theme.SkillsTheme
import com.motorro.statemachine.skills.app.ui.MainScreen

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

