package com.pegio.gymbro.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.pegio.gymbro.data.remote.core.FirebaseConstants.LEVEL
import com.pegio.gymbro.data.remote.core.FirebaseConstants.WORKOUT_PLANS
import com.pegio.gymbro.data.remote.core.FirestoreUtils
import com.pegio.gymbro.data.remote.model.WorkoutPlanDto
import com.pegio.gymbro.data.remote.model.mapper.WorkoutPlanDtoMapper
import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.core.convertList
import com.pegio.gymbro.domain.model.WorkoutPlan
import com.pegio.gymbro.domain.repository.WorkoutPlanRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class WorkoutPlanRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val firestoreUtils: FirestoreUtils,
    private val workoutPlanDtoMapper: WorkoutPlanDtoMapper
) : WorkoutPlanRepository {

    companion object {
        private const val WORKOUT_PLANS_PAGE_SIZE = 10L
    }

    override fun observeWorkoutPlansPagingStream(lastVisibleTitle: String?): Flow<Resource<List<WorkoutPlan>, DataError.Firestore>> {
        val query = db.collection(WORKOUT_PLANS)
//            .orderBy(LEVEL)
            .limit(WORKOUT_PLANS_PAGE_SIZE)

//        if (!lastVisibleTitle.isNullOrEmpty()) {
//            query = query.startAfter(lastVisibleTitle)
//        }

        return firestoreUtils.observeDocuments(query, WorkoutPlanDto::class.java)
            .convertList(workoutPlanDtoMapper::mapToDomain)
    }
}