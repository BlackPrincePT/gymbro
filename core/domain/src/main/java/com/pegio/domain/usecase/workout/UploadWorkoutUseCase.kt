package com.pegio.domain.usecase.workout

import com.pegio.auth.repository.AuthRepository
import com.pegio.common.core.Error
import com.pegio.common.core.Resource
import com.pegio.common.core.SessionError
import com.pegio.common.core.asFailure
import com.pegio.common.core.asSuccess
import com.pegio.firestore.repository.WorkoutRepository
import com.pegio.model.Workout
import javax.inject.Inject

class UploadWorkoutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val workoutRepository: WorkoutRepository
) {
    suspend operator fun invoke(
        title: String,
        description: String,
    ): Resource<Workout, Error> {

        val currentUser =
            authRepository.getCurrentUser() ?: return SessionError.Unauthenticated.asFailure()

        if (currentUser.isAnonymous)
            return SessionError.AnonymousUser.asFailure()

        val workout = Workout(
            id = "",
            authorId = currentUser.id,
            title = title,
            description = description
        )

        val id = workoutRepository.uploadWorkout(workout)

        val workoutWithId = workout.copy(id = id)

        return workoutWithId.asSuccess()

    }
}