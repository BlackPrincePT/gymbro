package com.pegio.domain.usecase.workout

import com.pegio.firestore.repository.WorkoutPlanRepository
import javax.inject.Inject

class FetchNextWorkoutPlansPageUseCase @Inject constructor(private val repository: WorkoutPlanRepository) {
    suspend operator fun invoke() = repository.fetchNextWorkoutPlansPage()
}