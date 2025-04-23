package com.pegio.firestore.repository.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.common.core.convertList
import com.pegio.firestore.repository.WorkoutPlanRepository
import com.pegio.firestore.core.FirestoreConstants.WORKOUT_ORDER
import com.pegio.firestore.core.FirestoreConstants.WORKOUT_PLANS
import com.pegio.firestore.model.WorkoutPlanDto
import com.pegio.firestore.model.mapper.WorkoutPlanDtoMapper
import com.pegio.firestore.util.FirestoreUtils
import com.pegio.model.WorkoutPlan
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class WorkoutPlanRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val firestoreUtils: FirestoreUtils,
    private val workoutPlanDtoMapper: WorkoutPlanDtoMapper
) : WorkoutPlanRepository {

    companion object {
        private const val WORKOUT_PLANS_PAGE_SIZE = 10L
    }

    override fun observeWorkoutPlansPagingStream(): Flow<Resource<List<WorkoutPlan>, DataError.Firestore>> {
        val query = db.collection(WORKOUT_PLANS)
            .orderBy(WORKOUT_ORDER)
            .limit(WORKOUT_PLANS_PAGE_SIZE)

        return firestoreUtils.observeDocuments(query, WorkoutPlanDto::class.java)
            .convertList(workoutPlanDtoMapper::mapToDomain)
    }
}