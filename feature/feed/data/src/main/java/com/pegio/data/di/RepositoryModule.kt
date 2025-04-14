package com.pegio.data.di

import com.pegio.data.repository.PostRepositoryImpl
import com.pegio.domain.repository.PostRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {

    @Binds
    fun bind(impl: PostRepositoryImpl): PostRepository
}