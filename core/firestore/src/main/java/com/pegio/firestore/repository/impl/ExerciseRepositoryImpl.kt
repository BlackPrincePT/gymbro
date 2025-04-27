package com.pegio.firestore.repository.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.common.core.map
import com.pegio.common.core.mapList
import com.pegio.firestore.core.FirestoreConstants.EXERCISES
import com.pegio.firestore.core.FirestoreConstants.WORKOUTS
import com.pegio.firestore.model.ExerciseDto
import com.pegio.firestore.model.mapper.ExerciseDtoMapper
import com.pegio.firestore.repository.ExerciseRepository
import com.pegio.firestore.util.FirestoreUtils
import com.pegio.model.Exercise
import javax.inject.Inject

internal class ExerciseRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val firestoreUtils: FirestoreUtils,
    private val exerciseDtoMapper: ExerciseDtoMapper
) : ExerciseRepository {

    override fun uploadExercises(exercises: List<Exercise>, workoutId: String) {
        val documentRef = db.collection(WORKOUTS).document(workoutId).collection(EXERCISES)
        exercises.forEach { exercise ->
            val exerciseDto = exerciseDtoMapper.mapFromDomain(exercise)
            documentRef.add(exerciseDto)
        }
    }

    override suspend fun fetchExercises(workoutId: String): Resource<List<Exercise>, DataError.Firestore> {
        val collectionRef = db.collection(WORKOUTS).document(workoutId).collection(EXERCISES)

        return firestoreUtils.queryDocuments(collectionRef, ExerciseDto::class.java)
            .map { it.objects }
            .mapList(exerciseDtoMapper::mapToDomain)
    }
}