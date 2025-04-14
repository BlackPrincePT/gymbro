package com.pegio.data.di

import com.pegio.data.repository.AiChatRepositoryImpl
import com.pegio.data.repository.AiMessagesRepositoryImpl
import com.pegio.domain.repository.AiChatRepository
import com.pegio.domain.repository.AiMessagesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AiChatRepositoryModule {

    @Binds
    abstract fun bindAiChatRepository(impl: AiChatRepositoryImpl) : AiChatRepository

    @Binds
    abstract fun bindAiMessagesRepository(impl: AiMessagesRepositoryImpl) : AiMessagesRepository
}