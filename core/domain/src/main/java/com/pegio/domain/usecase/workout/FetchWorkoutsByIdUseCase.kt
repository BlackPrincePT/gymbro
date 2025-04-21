package com.pegio.domain.usecase.workout

import com.pegio.domain.repository.WorkoutRepository
import javax.inject.Inject

class FetchWorkoutsByIdUseCase@Inject constructor(private val workoutRepository: WorkoutRepository) {
    suspend operator fun invoke(id:String) = workoutRepository.fetchWorkoutsById(id)
}