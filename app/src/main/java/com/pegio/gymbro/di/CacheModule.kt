package com.pegio.gymbro.di

import com.pegio.gymbro.data.local.datastore.DataStoreManager
import com.pegio.gymbro.domain.cache.CacheManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CacheModule {

    @Binds
    abstract fun bindCacheManager(dataStoreManager: DataStoreManager): CacheManager
}