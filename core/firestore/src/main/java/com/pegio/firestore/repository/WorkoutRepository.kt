package com.pegio.firestore.repository

import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.model.Workout

interface WorkoutRepository {
    suspend fun fetchWorkoutsById(id:String): Resource<List<Workout>, DataError.Firestore>
    suspend fun uploadWorkouts(authorId: String, workouts: List<Workout>)
}