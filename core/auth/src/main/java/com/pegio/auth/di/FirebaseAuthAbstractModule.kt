package com.pegio.auth.di

import com.pegio.auth.core.AuthHandler
import com.pegio.auth.core.FirebaseAuthHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class FirebaseAuthAbstractModule {

    @Binds
    abstract fun bindAuthHandler(impl: FirebaseAuthHandler): AuthHandler
}