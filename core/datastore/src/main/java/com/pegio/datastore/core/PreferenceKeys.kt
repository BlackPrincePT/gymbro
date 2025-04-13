package com.pegio.datastore.core

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey

sealed class PreferenceKey<T>(internal val value: Preferences.Key<T>) {
    data object AuthState : PreferenceKey<Boolean>(value = booleanPreferencesKey(name = "auth_state"))
}