package com.pegio.gymbro.common.domain.usecase.cache

import com.pegio.gymbro.common.domain.cache.CacheManager
import javax.inject.Inject

class ClearCacheUseCase @Inject constructor(private val cacheManager: CacheManager) {
    suspend operator fun invoke() = cacheManager.clear()
}