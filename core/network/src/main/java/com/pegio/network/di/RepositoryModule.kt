package com.pegio.network.di

import android.content.Context
import com.pegio.network.repository.AiChatRepositoryImpl
import com.pegio.domain.repository.AiChatRepository
import com.pegio.domain.repository.TextToSpeechRepository
import com.pegio.network.repository.TextToSpeechRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    abstract fun bindAiChatRepositoryImpl(impl: AiChatRepositoryImpl): AiChatRepository

    @Binds
    abstract fun provideTextToSpeechRepository(impl: TextToSpeechRepositoryImpl): TextToSpeechRepository
}