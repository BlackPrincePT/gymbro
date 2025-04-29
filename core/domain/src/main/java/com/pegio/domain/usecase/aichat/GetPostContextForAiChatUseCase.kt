package com.pegio.domain.usecase.aichat

import com.pegio.common.core.asFailure
import com.pegio.common.core.asSuccess
import com.pegio.common.core.getOrElse
import com.pegio.common.core.getOrNull
import com.pegio.common.core.retryableCall
import com.pegio.domain.model.AiChatContext
import com.pegio.domain.model.ExerciseContext
import com.pegio.domain.model.PostContext
import com.pegio.domain.model.WorkoutContext
import com.pegio.firestore.repository.ExerciseRepository
import com.pegio.firestore.repository.PostRepository
import com.pegio.firestore.repository.WorkoutRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetPostContextForAiChatUseCase @Inject constructor(
    private val postRepository: PostRepository,
    private val workoutRepository: WorkoutRepository,
    private val exerciseRepository: ExerciseRepository
) {
    suspend operator fun invoke(postId: String) = coroutineScope {
        val post = retryableCall { postRepository.fetchPostById(id = postId) }
            .getOrElse { return@coroutineScope it.asFailure() }

        val postContext = PostContext(
            content = post.content
        )

        var workoutContext: WorkoutContext? = null
        var exercisesContext: List<ExerciseContext>? = null

        post.workoutId?.let { workoutId ->
            val deferredWorkout = async {
                retryableCall { workoutRepository.fetchWorkoutById(id = workoutId) }
                    .getOrNull()
            }

            val deferredExercises = async {
                retryableCall { exerciseRepository.fetchExercises(workoutId) }
                    .getOrNull()
            }

            val workout = deferredWorkout.await()
            workout?.run {
                workoutContext = WorkoutContext(
                    title = title,
                    description = description
                )
            }

            val exercises = deferredExercises.await()
            if (!exercises.isNullOrEmpty()) {
                exercisesContext = exercises.map { exercise ->
                    ExerciseContext(
                        name = exercise.name,
                        description = exercise.description,
                        type = exercise.type.name,
                        value = exercise.value,
                        position = exercise.position,
                        sets = exercise.sets,
                        muscleGroups = exercise.muscleGroups.map { it.name }
                    )
                }
            }
        }

        return@coroutineScope AiChatContext(
            post = postContext,
            workout = workoutContext,
            exercises = exercisesContext
        )
            .asSuccess()
    }
}