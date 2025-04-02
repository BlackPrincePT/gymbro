package com.pegio.gymbro.di

import com.pegio.gymbro.BuildConfig
import com.pegio.gymbro.data.remote.api.AiChatApi
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
    fun provideRetrofit(): Retrofit {
        val json = Json {
            encodeDefaults = true
            ignoreUnknownKeys = true
        }

        return Retrofit.Builder()
            .baseUrl(BuildConfig.GEMINI_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideChatApi(retrofit: Retrofit): AiChatApi {
        return retrofit.create(AiChatApi::class.java)
    }
}