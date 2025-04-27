package com.pegio.domain.usecase.common

import com.pegio.datastore.core.CacheManager
import com.pegio.datastore.core.PreferenceKey
import javax.inject.Inject

class ObserveCachedThemeModeUseCase @Inject constructor(private val cacheManager: CacheManager) {
    operator fun invoke() = cacheManager.observe(PreferenceKey.ThemeMode)
}