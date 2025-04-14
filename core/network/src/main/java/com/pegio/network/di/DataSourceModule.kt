package com.pegio.network.di

import com.pegio.network.core.DefaultNetworkDataSource
import com.pegio.network.core.NetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataSourceModule {

    @Binds
    abstract fun bind(impl: DefaultNetworkDataSource): NetworkDataSource
}