package com.pegio.domain.usecase.workout

import com.pegio.auth.repository.AuthRepository
import com.pegio.common.core.Error
import com.pegio.common.core.Resource
import com.pegio.common.core.SessionError
import com.pegio.common.core.asFailure
import com.pegio.firestore.repository.WorkoutRepository
import com.pegio.model.Workout
import javax.inject.Inject

class FetchNextUserWorkoutsPageUseCase @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke() : Resource<List<Workout>, Error> {
        val currentUser =
            authRepository.getCurrentUser() ?: return SessionError.Unauthenticated.asFailure()

        if (currentUser.isAnonymous)
            return SessionError.AnonymousUser.asFailure()

        return workoutRepository.fetchNextUserWorkoutsPage(currentUser.id)
    }
}