package com.example.nimma_guru

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nimma_guru.ui.NimmaGuruApp
import com.example.nimma_guru.ui.theme.NimmaguruTheme
import com.example.nimma_guru.ui.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val isDarkThemePref by settingsViewModel.isDarkTheme.collectAsState()
            val language by settingsViewModel.language.collectAsState()

            val darkTheme = isDarkThemePref ?: isSystemInDarkTheme()

            // Apply Locale
            val locale = remember(language) { Locale(language) }
            val configuration = LocalConfiguration.current
            configuration.setLocale(locale)
            
            val context = LocalContext.current
            val resources = context.resources
            resources.updateConfiguration(configuration, resources.displayMetrics)

            CompositionLocalProvider(LocalConfiguration provides configuration) {
                NimmaguruTheme(darkTheme = darkTheme) {
                    NimmaGuruApp(settingsViewModel)
                }
            }
        }
    }
}
