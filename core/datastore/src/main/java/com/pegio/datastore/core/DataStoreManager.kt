package com.pegio.datastore.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

internal class DataStoreManager @Inject constructor(@ApplicationContext private val context: Context) : CacheManager {

    override fun <T> observe(key: PreferenceKey<T>): Flow<T?> {
        return context.dataStore.data.map { it[key.value] }
    }

    override suspend fun <T> save(key: PreferenceKey<T>, value: T) {
        context.dataStore.edit { it[key.value] = value }
    }

    override suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}