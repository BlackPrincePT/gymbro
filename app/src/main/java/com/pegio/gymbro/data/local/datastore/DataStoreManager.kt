package com.pegio.gymbro.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.pegio.gymbro.domain.manager.cache.CacheManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

class DataStoreManager @Inject constructor(@ApplicationContext private val context: Context) : CacheManager {

    override fun <T> observe(key: Preferences.Key<T>): Flow<T?> {
        return context.dataStore.data.map { it[key] }
    }

    override suspend fun <T> save(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { it[key] = value }
    }

    override suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}