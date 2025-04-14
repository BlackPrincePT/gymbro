package com.example.network.di

import com.example.network.service.AiChatService
import com.pegio.network.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideAiChatService(retrofit: Retrofit): AiChatService {
        return retrofit.create(AiChatService::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val json = Json {
            encodeDefaults = true
            ignoreUnknownKeys = true
        }

        return Retrofit.Builder()
            .baseUrl(BuildConfig.GPT_BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

}