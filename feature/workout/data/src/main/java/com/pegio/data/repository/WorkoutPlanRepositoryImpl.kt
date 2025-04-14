package com.pegio.data.repository

import com.pegio.data.model.mapper.WorkoutPlanDtoMapper
import com.pegio.domain.repository.WorkoutPlanRepository
import com.pegio.model.WorkoutPlan
import com.google.firebase.firestore.FirebaseFirestore
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.common.core.convertList
import com.pegio.firestore.core.FirestoreConstants.WORKOUT_ORDER
import com.pegio.firestore.core.FirestoreConstants.WORKOUT_PLANS
import com.pegio.firestore.model.WorkoutPlanDto
import com.pegio.firestore.util.FirestoreUtils
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

    override fun observeWorkoutPlansPagingStream(lastVisibleTitle: String?): Flow<Resource<List<WorkoutPlan>, DataError.Firestore>> {
        val query = db.collection(WORKOUT_PLANS)
            .orderBy(WORKOUT_ORDER)
            .limit(WORKOUT_PLANS_PAGE_SIZE)

//        if (!lastVisibleTitle.isNullOrEmpty()) {
//            query = query.startAfter(lastVisibleTitle)
//        }

        return firestoreUtils.observeDocuments(query, WorkoutPlanDto::class.java)
            .convertList(workoutPlanDtoMapper::mapToDomain)
    }
}