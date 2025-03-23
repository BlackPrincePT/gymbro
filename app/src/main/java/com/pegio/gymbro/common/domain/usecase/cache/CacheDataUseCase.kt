package com.pegio.gymbro.common.domain.usecase.cache

import androidx.datastore.preferences.core.Preferences
import com.pegio.gymbro.common.domain.cache.CacheManager
import javax.inject.Inject

class CacheDataUseCase @Inject constructor(private val cacheManager: CacheManager) {
    suspend operator fun <T> invoke(key: Preferences.Key<T>, value: T) =
        cacheManager.save(key = key, value = value)
}