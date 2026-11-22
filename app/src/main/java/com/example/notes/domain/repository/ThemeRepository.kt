package com.example.notes.domain.repository

import com.example.notes.domain.model.AppTheme
import kotlinx.coroutines.flow.Flow

interface ThemeRepository {

    val theme: Flow<AppTheme>

    suspend fun setTheme(theme: AppTheme)
}
