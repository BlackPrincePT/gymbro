package com.pegio.domain.usecase.workout

import com.pegio.firestore.repository.UserRepository
import com.pegio.firestore.repository.WorkoutRepository
import javax.inject.Inject

class FetchWorkoutsByUserIdUseCase @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val userRepository: UserRepository
) {

}
