package com.pegio.domain.usecase.workout

import com.pegio.firestore.repository.WorkoutRepository
import javax.inject.Inject

class FetchWorkoutByIdUseCase @Inject constructor(private val workoutRepository: WorkoutRepository) {
    suspend operator fun invoke(id: String) = workoutRepository.fetchWorkoutById(id)
}