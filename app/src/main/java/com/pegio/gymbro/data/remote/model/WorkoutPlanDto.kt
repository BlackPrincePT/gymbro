package com.pegio.gymbro.data.remote.model

import com.google.firebase.firestore.DocumentId
import com.pegio.gymbro.domain.model.WorkoutPlan.DifficultyLevel

data class WorkoutPlanDto(
    @DocumentId val id: String = "",
    val level: DifficultyLevel = DifficultyLevel.EASY,
    val title: String = "",
    val description: String = "",
    val duration: String = "",
    val intensity: String = "",
    val imageUrl: String = ""
)