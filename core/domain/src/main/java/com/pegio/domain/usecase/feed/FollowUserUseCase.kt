package com.pegio.domain.usecase.feed

import com.pegio.auth.repository.AuthRepository
import com.pegio.common.core.Resource
import com.pegio.common.core.SessionError
import com.pegio.common.core.asFailure
import com.pegio.common.core.asSuccess
import com.pegio.firestore.repository.FollowRecordRepository
import javax.inject.Inject

class FollowUserUseCase @Inject constructor(
    private val followRecordRepository: FollowRecordRepository,
    private val authRepository: AuthRepository
) {
    operator fun invoke(targetUserId: String): Resource<Unit, SessionError> {
        val currentUser =
            authRepository.getCurrentUser() ?: return SessionError.Unauthenticated.asFailure()

        if (currentUser.isAnonymous)
            return SessionError.AnonymousUser.asFailure()

        followRecordRepository.followUser(userId = currentUser.id, targetUserId = targetUserId)

        return Unit.asSuccess()
    }
}