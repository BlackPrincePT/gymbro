package com.pegio.firestore.model

import com.google.firebase.firestore.DocumentId
import com.pegio.model.Exercise.Type
import com.pegio.model.Exercise.MuscleGroup

data class WorkoutDto(
    @DocumentId val id: String? = null,
    val authorId:String = "",
    val title: String = "",
    val description: String = ""
)