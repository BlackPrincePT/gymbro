package com.pegio.datastore.core

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

sealed class PreferenceKey<T>(internal val value: Preferences.Key<T>) {
    data object ThemeMode : PreferenceKey<String>(value = stringPreferencesKey(name = "theme-mode"))
}