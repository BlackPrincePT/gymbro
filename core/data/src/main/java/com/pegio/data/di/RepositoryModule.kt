package com.pegio.data.di

import com.pegio.data.repository.AiChatRepositoryImpl
import com.pegio.data.repository.AiMessagesRepositoryImpl
import com.pegio.data.repository.AuthRepositoryImpl
import com.pegio.data.repository.PostRepositoryImpl
import com.pegio.data.repository.UserRepositoryImpl
import com.pegio.data.repository.WorkoutPlanRepositoryImpl
import com.pegio.domain.repository.AiChatRepository
import com.pegio.domain.repository.AiMessagesRepository
import com.pegio.domain.repository.AuthRepository
import com.pegio.domain.repository.PostRepository
import com.pegio.domain.repository.UserRepository
import com.pegio.domain.repository.WorkoutPlanRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    // ========= Common ========= \\

    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    // ========= Ai Chat ========= \\

    @Binds
    abstract fun bindAiChatRepository(impl: AiChatRepositoryImpl) : AiChatRepository

    @Binds
    abstract fun bindAiMessagesRepository(impl: AiMessagesRepositoryImpl) : AiMessagesRepository

    // ========= Post ========= \\

    @Binds
    abstract fun bindPostRepository(impl: PostRepositoryImpl): PostRepository

    // ========= Workout ========= \\

    @Binds
    abstract fun bindWorkoutPlanRepository(impl: WorkoutPlanRepositoryImpl): WorkoutPlanRepository
}