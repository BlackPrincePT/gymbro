package com.pegio.domain.usecase.workout

import com.pegio.common.core.Error
import com.pegio.common.core.Resource
import com.pegio.common.core.asSuccess
import com.pegio.common.core.getOrNull
import com.pegio.common.core.isFailure
import com.pegio.common.core.map
import com.pegio.firestore.repository.ExerciseRepository
import com.pegio.model.Exercise
import com.pegio.uploadmanager.core.UploadManager
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class UploadExerciseUseCase @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val uploadManager: UploadManager
) {
    suspend operator fun invoke(
        exercises: List<Exercise>, workoutId:String
    ): Resource<Unit, Error> = coroutineScope {

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

        exerciseRepository.uploadExercises(
            workoutId = workoutId,
            exercises = successfulWorkouts
        )

        Unit.asSuccess()
    }

}

