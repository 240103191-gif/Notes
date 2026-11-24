package com.example.notes.presentation.features.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.domain.model.AppTheme
import com.example.notes.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeChangeViewModel(
    private val themeRepository: ThemeRepository,
) : ViewModel() {

    val state: StateFlow<ThemeChangeState> = themeRepository.theme
        .map { ThemeChangeState(selectedTheme = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ThemeChangeState(),
        )

    fun onThemeSelect(theme: AppTheme) {
        viewModelScope.launch {
            themeRepository.setTheme(theme)
        }
    }
}
