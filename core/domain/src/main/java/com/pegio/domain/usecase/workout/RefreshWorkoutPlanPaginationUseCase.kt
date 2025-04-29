package com.pegio.domain.usecase.workout

import com.pegio.firestore.repository.WorkoutPlanRepository
import javax.inject.Inject

class RefreshWorkoutPlanPaginationUseCase @Inject constructor(private val workoutPlanRepository: WorkoutPlanRepository) {
    operator fun invoke() = workoutPlanRepository.refreshPagination()
}