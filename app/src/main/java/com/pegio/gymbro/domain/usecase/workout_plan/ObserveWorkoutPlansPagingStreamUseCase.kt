package com.pegio.gymbro.domain.usecase.workout_plan

import com.pegio.gymbro.domain.repository.WorkoutPlanRepository
import javax.inject.Inject

class ObserveWorkoutPlansPagingStreamUseCase @Inject constructor(
    private val repository: WorkoutPlanRepository
) {
    operator fun invoke(lastVisibleTitle: String? = null) =
         repository.observeWorkoutPlansPagingStream(lastVisibleTitle)
}