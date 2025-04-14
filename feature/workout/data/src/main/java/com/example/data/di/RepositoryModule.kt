package com.example.data.di

import com.example.data.repository.WorkoutPlanRepositoryImpl
import com.example.domain.repository.WorkoutPlanRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {

    @Binds
    fun bind(impl: WorkoutPlanRepositoryImpl): WorkoutPlanRepository
}