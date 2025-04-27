package com.pegio.firestore.repository

import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.model.Workout

interface WorkoutRepository {
    suspend fun uploadWorkout(workout: Workout): String
    suspend fun fetchNextUserWorkoutsPage(authorId: String): Resource<List<Workout>, DataError>
}