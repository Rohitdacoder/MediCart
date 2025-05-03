package com.example.pov_clone_project.Database

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow
import androidx.datastore.preferences.core.edit

val Context.dataStore by preferencesDataStore("settings")

object ThemePreference {
    private val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")

    fun getTheme(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { prefs -> prefs[DARK_THEME_KEY] ?: false }
    }

    suspend fun saveTheme(context: Context, isDark: Boolean) {
        context.dataStore.edit { it[DARK_THEME_KEY] = isDark }
    }
}
