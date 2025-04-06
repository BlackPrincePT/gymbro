package com.pegio.gymbro.di

import com.pegio.gymbro.data.repository.AuthRepositoryImpl
import com.pegio.gymbro.data.repository.AiChatRepositoryImpl
import com.pegio.gymbro.data.repository.AiMessagesRepositoryImpl
import com.pegio.gymbro.data.repository.PostRepositoryImpl
import com.pegio.gymbro.data.repository.UserRepositoryImpl
import com.pegio.gymbro.domain.repository.AiMessagesRepository
import com.pegio.gymbro.domain.repository.AuthRepository
import com.pegio.gymbro.domain.repository.AiChatRepository
import com.pegio.gymbro.domain.repository.PostRepository
import com.pegio.gymbro.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(repository: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun bindUserRepository(repository: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindChatRepository(repository: AiChatRepositoryImpl): AiChatRepository

    @Binds
    abstract fun bindAiFireStoreMessageRepository(repository: AiMessagesRepositoryImpl): AiMessagesRepository

    @Binds
    abstract fun bindPostRepository(repository: PostRepositoryImpl): PostRepository
}