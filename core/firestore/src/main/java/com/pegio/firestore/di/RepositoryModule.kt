package com.pegio.firestore.di

import com.pegio.firestore.repository.AiMessagesRepository
import com.pegio.firestore.repository.FcmTokenRepository
import com.pegio.firestore.repository.FollowerRepository
import com.pegio.firestore.repository.PostCommentRepository
import com.pegio.firestore.repository.PostRepository
import com.pegio.firestore.repository.UserRepository
import com.pegio.firestore.repository.VoteRepository
import com.pegio.firestore.repository.WorkoutPlanRepository
import com.pegio.firestore.repository.WorkoutRepository
import com.pegio.firestore.repository.impl.AiMessagesRepositoryImpl
import com.pegio.firestore.repository.impl.FcmTokenRepositoryImpl
import com.pegio.firestore.repository.impl.FollowerRepositoryImpl
import com.pegio.firestore.repository.impl.PostCommentRepositoryImpl
import com.pegio.firestore.repository.impl.PostRepositoryImpl
import com.pegio.firestore.repository.impl.UserRepositoryImpl
import com.pegio.firestore.repository.impl.VoteRepositoryImpl
import com.pegio.firestore.repository.impl.WorkoutPlanRepositoryImpl
import com.pegio.firestore.repository.impl.WorkoutRepositoryImpl
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
    abstract fun bindFcmTokenRepository(impl: FcmTokenRepositoryImpl): FcmTokenRepository

    @Binds
    abstract fun bindAiMessagesRepository(impl: AiMessagesRepositoryImpl): AiMessagesRepository

    @Binds
    abstract fun bindPostRepository(impl: PostRepositoryImpl): PostRepository

    @Binds
    abstract fun bindPostCommentRepository(impl: PostCommentRepositoryImpl): PostCommentRepository

    @Binds
    abstract fun bindVoteRepository(impl: VoteRepositoryImpl): VoteRepository

    @Binds
    abstract fun bindFollowerRepository(impl: FollowerRepositoryImpl): FollowerRepository

    @Binds
    abstract fun bindWorkoutPlanRepository(impl: WorkoutPlanRepositoryImpl): WorkoutPlanRepository

    @Binds
    abstract fun bindWorkoutRepository(impl: WorkoutRepositoryImpl): WorkoutRepository
}