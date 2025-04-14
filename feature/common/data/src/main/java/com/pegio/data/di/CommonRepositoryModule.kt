package com.pegio.data.di

import com.pegio.data.repository.AuthRepositoryImpl
import com.pegio.data.repository.UserRepositoryImpl
import com.pegio.domain.repository.AuthRepository
import com.pegio.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CommonRepositoryModule {

    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}