package com.pegio.firestore.di

import com.pegio.domain.repository.AiMessagesRepository
import com.pegio.domain.repository.PostCommentRepository
import com.pegio.domain.repository.PostRepository
import com.pegio.domain.repository.UserRepository
import com.pegio.domain.repository.WorkoutPlanRepository
import com.pegio.domain.repository.WorkoutRepository
import com.pegio.firestore.repository.AiMessagesRepositoryImpl
import com.pegio.firestore.repository.PostCommentRepositoryImpl
import com.pegio.firestore.repository.PostRepositoryImpl
import com.pegio.firestore.repository.UserRepositoryImpl
import com.pegio.firestore.repository.WorkoutPlanRepositoryImpl
import com.pegio.firestore.repository.WorkoutRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindAiMessagesRepository(impl: AiMessagesRepositoryImpl) : AiMessagesRepository

    @Binds
    abstract fun bindPostRepository(impl: PostRepositoryImpl): PostRepository

    @Binds
    abstract fun bindPostCommentRepository(impl: PostCommentRepositoryImpl): PostCommentRepository

    @Binds
    abstract fun bindWorkoutPlanRepository(impl: WorkoutPlanRepositoryImpl): WorkoutPlanRepository

    @Binds
    abstract fun bindWorkoutRepository(impl: WorkoutRepositoryImpl): WorkoutRepository
}