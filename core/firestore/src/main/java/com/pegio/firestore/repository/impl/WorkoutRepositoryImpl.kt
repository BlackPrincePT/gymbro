package com.pegio.firestore.repository.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.common.core.map
import com.pegio.firestore.core.FirestoreConstants.WORKOUTS
import com.pegio.firestore.model.WorkoutDto
import com.pegio.firestore.model.mapper.ExerciseDtoMapper
import com.pegio.firestore.repository.WorkoutRepository
import com.pegio.firestore.util.FirestoreUtils
import com.pegio.model.Workout
import javax.inject.Inject

internal class WorkoutRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val firestoreUtils: FirestoreUtils,
    private val exerciseDtoMapper: ExerciseDtoMapper
) : WorkoutRepository {

    override suspend fun fetchWorkoutsById(id: String): Resource<List<Workout>, DataError.Firestore> {
        val documentRef = db.collection(WORKOUTS).document(id)

        val result = firestoreUtils.readDocument(documentRef, WorkoutDto::class.java)

        return result.map { container ->
            container.workouts.map(exerciseDtoMapper::mapToDomain)
        }
    }

}