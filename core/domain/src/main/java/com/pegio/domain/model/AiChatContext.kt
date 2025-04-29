package com.pegio.domain.model

import com.pegio.model.User.Gender
import kotlinx.serialization.Serializable

@Serializable
data class AiChatContext(
    val post: PostContext,
    val workout: WorkoutContext? = null,
    val exercises: List<ExerciseContext>? = null
)

@Serializable
data class UserContext(
    val username: String,
    val age: Int,
    val gender: Gender,
    val heightCm: Int,
    val weightKg: Int,
)

@Serializable
data class PostContext(
    val content: String
)

@Serializable
data class WorkoutContext(
    val title: String,
    val description: String
)

@Serializable
data class ExerciseContext(
    val name: String,
    val description: String,
    val type: String,
    val value: Int,
    val position: Int,
    val sets: Int,
    val muscleGroups: List<String>
)