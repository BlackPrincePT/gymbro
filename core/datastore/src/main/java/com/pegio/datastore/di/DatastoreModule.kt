package com.pegio.datastore.di

import com.pegio.datastore.core.CacheManager
import com.pegio.datastore.core.DataStoreManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatastoreModule {

    @Binds
    @Singleton
    abstract fun bindDatastore(dataStoreManager: DataStoreManager): CacheManager
}