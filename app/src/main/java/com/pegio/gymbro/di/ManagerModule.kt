package com.pegio.gymbro.di

import com.pegio.gymbro.data.local.datastore.DataStoreManager
import com.pegio.gymbro.data.workmanager.UploadManager
import com.pegio.gymbro.domain.manager.cache.CacheManager
import com.pegio.gymbro.domain.manager.upload.FileUploadManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ManagerModule {

    @Binds
    abstract fun bindCacheManager(dataStoreManager: DataStoreManager): CacheManager

    @Binds
    abstract fun bindFileUploadManager(uploadManager: UploadManager): FileUploadManager
}