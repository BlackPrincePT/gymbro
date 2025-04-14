package com.example.data.di

import com.example.data.repository.AiChatRepositoryImpl
import com.example.data.repository.AiMessagesRepositoryImpl
import com.example.domain.repository.AiChatRepository
import com.example.domain.repository.AiMessagesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAiChatRepository(impl: AiChatRepositoryImpl) : AiChatRepository

    @Binds
    abstract fun bindAiMessagesRepository(impl: AiMessagesRepositoryImpl) : AiMessagesRepository
}