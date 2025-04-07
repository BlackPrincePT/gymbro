package com.pegio.gymbro.domain.repository

import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.model.WorkoutPlan
import kotlinx.coroutines.flow.Flow

interface WorkoutPlanRepository {
    fun observeWorkoutPlansPagingStream(
        lastVisibleTitle: String? = null
    ): Flow<Resource<List<WorkoutPlan>, DataError.Firestore>>
}