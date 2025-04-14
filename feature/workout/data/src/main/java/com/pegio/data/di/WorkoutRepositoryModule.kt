package com.pegio.data.di

import com.pegio.data.repository.WorkoutPlanRepositoryImpl
import com.pegio.domain.repository.WorkoutPlanRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class WorkoutRepositoryModule {

    @Binds
    abstract fun bind(impl: WorkoutPlanRepositoryImpl): WorkoutPlanRepository
}