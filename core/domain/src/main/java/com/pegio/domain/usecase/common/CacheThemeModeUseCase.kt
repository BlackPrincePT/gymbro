package com.pegio.domain.usecase.common

import com.pegio.datastore.core.CacheManager
import com.pegio.datastore.core.PreferenceKey
import javax.inject.Inject

class CacheThemeModeUseCase @Inject constructor(private val cacheManager: CacheManager) {
    suspend operator fun invoke(value: String) {
        cacheManager.save(key = PreferenceKey.ThemeMode, value = value)
    }
}