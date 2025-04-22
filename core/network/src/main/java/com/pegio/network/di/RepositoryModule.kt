package com.pegio.network.di

import com.pegio.network.repository.AiChatRepositoryImpl
import com.pegio.network.repository.AiChatRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    abstract fun bindAiChatRepositoryImpl(impl: AiChatRepositoryImpl): AiChatRepository
}