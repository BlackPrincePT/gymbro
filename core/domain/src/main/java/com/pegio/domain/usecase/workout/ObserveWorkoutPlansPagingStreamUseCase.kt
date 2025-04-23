package com.pegio.domain.usecase.workout

import com.pegio.firestore.repository.WorkoutPlanRepository
import javax.inject.Inject

class ObserveWorkoutPlansPagingStreamUseCase @Inject constructor(private val repository: WorkoutPlanRepository) {
    operator fun invoke() = repository.observeWorkoutPlansPagingStream()
}