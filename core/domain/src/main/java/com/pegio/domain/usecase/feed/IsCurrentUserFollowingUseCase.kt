package com.pegio.domain.usecase.feed

import com.pegio.auth.repository.AuthRepository
import com.pegio.common.core.Error
import com.pegio.common.core.Resource
import com.pegio.common.core.SessionError
import com.pegio.common.core.asFailure
import com.pegio.common.core.map
import com.pegio.common.core.retryableCall
import com.pegio.firestore.repository.FollowRecordRepository
import javax.inject.Inject

class IsCurrentUserFollowingUseCase @Inject constructor(
    private val followRecordRepository: FollowRecordRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(targetUserId: String): Resource<Unit, Error> {
        val currentUser =
            authRepository.getCurrentUser() ?: return SessionError.Unauthenticated.asFailure()

        if (currentUser.isAnonymous)
            return SessionError.AnonymousUser.asFailure()

        return retryableCall { followRecordRepository.isUserFollowing(userId = currentUser.id, targetUserId = targetUserId) }
            .map { Unit }
    }
}