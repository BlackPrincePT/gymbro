package com.pegio.gymbro.domain.manager.cache

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface CacheManager {

    fun <T> observe(key: Preferences.Key<T>): Flow<T?>

    suspend fun <T> save(key: Preferences.Key<T>, value: T)

    suspend fun clear()
}