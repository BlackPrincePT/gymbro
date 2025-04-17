package com.pegio.auth.di

import com.pegio.auth.repository.FirebaseAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthHandler(impl: FirebaseAuthRepository): com.pegio.domain.repository.AuthRepository
}