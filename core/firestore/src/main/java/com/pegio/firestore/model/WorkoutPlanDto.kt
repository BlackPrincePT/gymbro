package com.pegio.firestore.model

import com.example.model.WorkoutPlan.DifficultyLevel
import com.google.firebase.firestore.DocumentId

data class WorkoutPlanDto(
    @DocumentId val id: String = "",
    val level: DifficultyLevel = DifficultyLevel.EASY,
    val title: String = "",
    val description: String = "",
    val duration: String = "",
    val intensity: String = "",
    val imageUrl: String = ""
)