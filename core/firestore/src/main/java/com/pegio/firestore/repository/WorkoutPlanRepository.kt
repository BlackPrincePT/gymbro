package com.pegio.firestore.repository

import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.model.WorkoutPlan

interface WorkoutPlanRepository {
    suspend fun fetchNextWorkoutPlansPage(): Resource<List<WorkoutPlan>, DataError>
    fun refreshPagination()
}