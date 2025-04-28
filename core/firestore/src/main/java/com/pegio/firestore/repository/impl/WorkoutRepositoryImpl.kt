package com.pegio.firestore.repository.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.common.core.mapList
import com.pegio.firestore.core.FirestoreConstants.AUTHOR_ID
import com.pegio.firestore.core.FirestoreConstants.WORKOUTS
import com.pegio.firestore.core.FirestorePagingSource
import com.pegio.firestore.model.WorkoutDto
import com.pegio.firestore.model.mapper.WorkoutDtoMapper
import com.pegio.firestore.repository.WorkoutRepository
import com.pegio.firestore.util.FirestoreUtils
import com.pegio.model.Workout
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class WorkoutRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val workoutDtoMapper: WorkoutDtoMapper,
    firestoreUtils: FirestoreUtils
) : WorkoutRepository {

    companion object {
        const val WORKOUTS_PAGE_SIZE: Long = 10L
    }

    private val workoutsPagingSource =
        FirestorePagingSource(WORKOUTS_PAGE_SIZE, WorkoutDto::class.java, firestoreUtils)

    override suspend fun uploadWorkout(workout: Workout): String {
        val workoutDto = workoutDtoMapper.mapFromDomain(workout)
        val documentRef = db.collection(WORKOUTS).add(workoutDto).await()
        return documentRef.id
    }

    override suspend fun fetchNextUserWorkoutsPage(authorId: String): Resource<List<Workout>, DataError> {
        val baseQuery = db.collection(WORKOUTS)
            .whereEqualTo(AUTHOR_ID, authorId)

        return workoutsPagingSource.loadNextPage(baseQuery)
            .mapList(workoutDtoMapper::mapToDomain)
    }
}