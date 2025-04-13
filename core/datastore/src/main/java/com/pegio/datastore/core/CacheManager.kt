package com.pegio.datastore.core

import kotlinx.coroutines.flow.Flow

interface CacheManager {

    fun <T> observe(key: PreferenceKey<T>): Flow<T?>

    suspend fun <T> save(key: PreferenceKey<T>, value: T)

    suspend fun clear()
}