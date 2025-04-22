package com.pegio.firestore.repository

import com.pegio.model.WorkoutPlan
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import kotlinx.coroutines.flow.Flow

interface WorkoutPlanRepository {
    fun observeWorkoutPlansPagingStream(): Flow<Resource<List<WorkoutPlan>, DataError.Firestore>>
}