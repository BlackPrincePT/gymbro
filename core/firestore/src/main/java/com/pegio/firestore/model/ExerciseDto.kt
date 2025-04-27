package com.pegio.firestore.model

import com.google.firebase.firestore.DocumentId
import com.pegio.model.Exercise.MuscleGroup
import com.pegio.model.Exercise.Type

data class ExerciseDto(
    @DocumentId val id: String? = null,
    val position: Int = 0,
    val name: String = "",
    val description: String = "",
    val type: Type = Type.REPETITION,
    val value: Int = 0,
    val sets: Int = 1,
    val muscleGroups: List<MuscleGroup> = emptyList(),
    val workoutImage: String = ""
)