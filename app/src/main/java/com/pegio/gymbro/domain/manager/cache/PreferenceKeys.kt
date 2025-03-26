package com.pegio.gymbro.domain.manager.cache

import androidx.datastore.preferences.core.booleanPreferencesKey

object PreferenceKeys {
    val AUTH_STATE_KEY = booleanPreferencesKey(name = "auth_state")
}