package com.pegio.firestore.repository

import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.model.Exercise

interface ExerciseRepository {
    fun uploadExercises(exercises: List<Exercise>, workoutId: String)
    suspend fun fetchExercises(workoutId: String): Resource<List<Exercise>, DataError.Firestore>
}