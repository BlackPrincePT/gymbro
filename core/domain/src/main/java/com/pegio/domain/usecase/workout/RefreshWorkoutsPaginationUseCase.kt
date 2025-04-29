package com.pegio.domain.usecase.workout

import com.pegio.firestore.repository.WorkoutRepository
import javax.inject.Inject

class RefreshWorkoutsPaginationUseCase @Inject constructor(private val workoutRepository: WorkoutRepository) {
    operator fun invoke() = workoutRepository.refreshPagination()
}