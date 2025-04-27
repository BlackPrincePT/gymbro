package com.pegio.domain.usecase.workout

import com.pegio.auth.repository.AuthRepository
import com.pegio.common.core.Error
import com.pegio.common.core.Resource
import com.pegio.common.core.SessionError
import com.pegio.common.core.asFailure
import com.pegio.common.core.asSuccess
import com.pegio.common.core.getOrNull
import com.pegio.common.core.isFailure
import com.pegio.common.core.map
import com.pegio.firestore.repository.WorkoutRepository
import com.pegio.model.Exercise
import com.pegio.uploadmanager.core.UploadManager
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class UploadWorkoutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val workoutRepository: WorkoutRepository,
    private val uploadManager: UploadManager
) {
    suspend operator fun invoke(
        exercises: List<Exercise>
    ): Resource<Unit, Error> = coroutineScope {

        val currentUser = authRepository.getCurrentUser()
            ?: return@coroutineScope SessionError.Unauthenticated.asFailure()

        if (currentUser.isAnonymous)
            return@coroutineScope SessionError.AnonymousUser.asFailure()

        val updatedWorkouts = exercises.map { workout ->
            async {
                uploadManager.enqueueFileUpload(workout.workoutImage)
                    .map { uploadedUrl ->
                        workout.copy(workoutImage = uploadedUrl)
                    }
            }
        }.awaitAll()

        updatedWorkouts.firstOrNull { it.isFailure() }
            ?.let { return@coroutineScope it as Resource.Failure }

        val successfulWorkouts = updatedWorkouts.mapNotNull { it.getOrNull() }

        workoutRepository.uploadWorkout(
            authorId = currentUser.id,
            workouts = successfulWorkouts
        )

        Unit.asSuccess()
    }

}

