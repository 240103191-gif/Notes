package com.example.notes.presentation.features.theme

import com.example.notes.domain.model.AppTheme

data class ThemeChangeState(
    val selectedTheme: AppTheme = AppTheme.DARK,
)
