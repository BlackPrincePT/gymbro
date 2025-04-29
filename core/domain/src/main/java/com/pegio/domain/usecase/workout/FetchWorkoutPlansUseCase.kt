package com.pegio.domain.usecase.workout

import com.pegio.firestore.repository.WorkoutPlanRepository
import javax.inject.Inject

class FetchWorkoutPlansUseCase @Inject constructor(private val repository: WorkoutPlanRepository) {
    suspend operator fun invoke() = repository.fetchWorkoutPlans()
}