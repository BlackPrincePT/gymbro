package com.example.domain.repository

import com.example.model.WorkoutPlan
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import kotlinx.coroutines.flow.Flow

interface WorkoutPlanRepository {
    fun observeWorkoutPlansPagingStream(
        lastVisibleTitle: String? = null
    ): Flow<Resource<List<WorkoutPlan>, DataError.Firestore>>
}