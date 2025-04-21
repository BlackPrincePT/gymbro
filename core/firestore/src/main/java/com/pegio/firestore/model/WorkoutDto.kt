package com.pegio.firestore.model

import com.google.firebase.firestore.DocumentId
import com.pegio.model.Workout.WorkoutType
import com.pegio.model.Workout.MuscleGroup



data class WorkoutContainerDto(
    val workouts: List<WorkoutDto> = emptyList()
)

data class WorkoutDto(
    @DocumentId val id: String = "",
    val name: String = "",
    val description: String = "",
    val workoutType: WorkoutType = WorkoutType.REPETITION,
    val value: Int = 0,
    val sets: Int = 1,
    val isFinished: Boolean = false,
    val muscleGroups: List<MuscleGroup> = emptyList(),
    val workoutImage: String = ""
)
