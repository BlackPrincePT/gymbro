package com.pegio.gymbro.data.remote.model

import com.google.firebase.firestore.DocumentId

data class WorkoutPlanDto(
    @DocumentId val id: String = "",
    val level: String = "",
    val title: String = "",
    val description: String = "",
    val duration: String = "",
    val intensity: String = "",
    val imageUrl: String = ""
)