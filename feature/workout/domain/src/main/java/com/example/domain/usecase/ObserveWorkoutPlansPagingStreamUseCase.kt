package com.example.domain.usecase

import com.example.domain.repository.WorkoutPlanRepository
import javax.inject.Inject

class ObserveWorkoutPlansPagingStreamUseCase @Inject constructor(
    private val repository: WorkoutPlanRepository
) {
    operator fun invoke(lastVisibleTitle: String? = null) =
         repository.observeWorkoutPlansPagingStream(lastVisibleTitle)
}