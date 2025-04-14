package com.example.network.di

import com.example.network.core.DefaultNetworkDataSource
import com.example.network.core.NetworkDataSource
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