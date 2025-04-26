package com.pegio.datastore.core

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey

sealed class PreferenceKey<T>(internal val value: Preferences.Key<T>) {
    data class RegistrationState(val userId: String) : PreferenceKey<Boolean>(value = booleanPreferencesKey(name = "isRegistrationCompleteFor-$userId"))
}