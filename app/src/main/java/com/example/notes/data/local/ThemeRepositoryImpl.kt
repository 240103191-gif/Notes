package com.example.notes.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.notes.domain.model.AppTheme
import com.example.notes.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val APP_THEME_KEY = stringPreferencesKey("app_theme")

class ThemeRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : ThemeRepository {

    override val theme: Flow<AppTheme> = dataStore.data.map { preferences ->
        AppTheme.fromKey(preferences[APP_THEME_KEY])
    }

    override suspend fun setTheme(theme: AppTheme) {
        dataStore.edit { preferences ->
            preferences[APP_THEME_KEY] = theme.key
        }
    }
}
