package com.example.nimma_guru.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _isDarkTheme = MutableStateFlow<Boolean?>(null) // null means follow system
    val isDarkTheme: StateFlow<Boolean?> = _isDarkTheme

    private val _language = MutableStateFlow("en") // "en" or "kn"
    val language: StateFlow<String> = _language

    fun toggleTheme(isDark: Boolean) {
        _isDarkTheme.value = isDark
    }

    fun setLanguage(lang: String) {
        _language.value = lang
    }
}
