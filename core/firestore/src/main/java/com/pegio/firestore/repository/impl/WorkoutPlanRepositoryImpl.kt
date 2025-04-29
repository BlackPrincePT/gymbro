package com.pegio.firestore.repository.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.common.core.mapList
import com.pegio.firestore.core.FirestoreConstants.WORKOUT_ORDER
import com.pegio.firestore.core.FirestoreConstants.WORKOUT_PLANS
import com.pegio.firestore.core.FirestorePagingSource
import com.pegio.firestore.model.WorkoutPlanDto
import com.pegio.firestore.model.mapper.WorkoutPlanDtoMapper
import com.pegio.firestore.repository.WorkoutPlanRepository
import com.pegio.firestore.util.FirestoreUtils
import com.pegio.model.WorkoutPlan
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class WorkoutPlanRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val workoutPlanDtoMapper: WorkoutPlanDtoMapper,
    firestoreUtils: FirestoreUtils
) : WorkoutPlanRepository {

    companion object {
        private const val WORKOUT_PLANS_PAGE_SIZE = 10L
    }

    private val workoutsPagingSource =
        FirestorePagingSource(WORKOUT_PLANS_PAGE_SIZE, WorkoutPlanDto::class.java, firestoreUtils)

    override suspend fun fetchNextWorkoutPlansPage(): Resource<List<WorkoutPlan>, DataError> {
        val query = db.collection(WORKOUT_PLANS)
            .orderBy(WORKOUT_ORDER)


        return workoutsPagingSource.loadNextPage(query)
            .mapList(workoutPlanDtoMapper::mapToDomain)
    }

    override fun refreshPagination() {
        workoutsPagingSource.resetPagination()
    }

}