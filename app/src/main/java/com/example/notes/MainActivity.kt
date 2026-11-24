package com.example.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.notes.domain.model.AppTheme
import com.example.notes.domain.repository.ThemeRepository
import com.example.notes.presentation.navigation.AppNavHost
import com.example.notes.presentation.theme.NotesTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val themeRepository: ThemeRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appTheme by themeRepository.theme.collectAsStateWithLifecycle(
                initialValue = AppTheme.DARK,
            )
            NotesTheme(appTheme = appTheme) {
                AppNavHost()
            }
        }
    }
}
