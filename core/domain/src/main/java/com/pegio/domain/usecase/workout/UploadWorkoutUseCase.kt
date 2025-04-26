package com.pegio.domain.usecase.workout

import com.pegio.auth.repository.AuthRepository
import com.pegio.common.core.Error
import com.pegio.common.core.Resource
import com.pegio.common.core.SessionError
import com.pegio.common.core.asFailure
import com.pegio.common.core.asSuccess
import com.pegio.common.core.getOrElse
import com.pegio.firestore.repository.WorkoutRepository
import com.pegio.model.Workout
import com.pegio.uploadmanager.core.UploadManager
import javax.inject.Inject

class UploadWorkoutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val workoutRepository: WorkoutRepository,
    private val uploadManager: UploadManager
) {
    suspend operator fun invoke(
        workouts: List<Workout>
    ): Resource<Unit, Error> {

        val currentUser = authRepository.getCurrentUser()
            ?: return SessionError.Unauthenticated.asFailure()

        if (currentUser.isAnonymous)
            return SessionError.AnonymousUser.asFailure()

        val updatedWorkouts = workouts.map { workout ->
            uploadManager.enqueueFileUpload(workout.workoutImage)
                .getOrElse { return it.asFailure() }
                .let { uploadedUrl ->
                    workout.copy(workoutImage = uploadedUrl)
                }
        }

        workoutRepository.uploadWorkouts(
            authorId = currentUser.id,
            workouts = updatedWorkouts
        )

        return Unit.asSuccess()
    }
}

