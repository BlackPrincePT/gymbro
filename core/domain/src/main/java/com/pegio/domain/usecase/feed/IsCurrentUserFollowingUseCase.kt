package com.pegio.domain.usecase.feed

import com.pegio.common.core.Error
import com.pegio.common.core.Resource
import com.pegio.common.core.SessionError
import com.pegio.common.core.asFailure
import com.pegio.common.core.map
import com.pegio.common.core.retryableCall
import com.pegio.domain.repository.AuthRepository
import com.pegio.domain.repository.FollowerRepository
import javax.inject.Inject

class IsCurrentUserFollowingUseCase @Inject constructor(
    private val followerRepository: FollowerRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(targetUserId: String): Resource<Unit, Error> {
        val currentUser =
            authRepository.getCurrentUser() ?: return SessionError.Unauthenticated.asFailure()

        if (currentUser.isAnonymous)
            return SessionError.AnonymousUser.asFailure()

        return retryableCall { followerRepository.isUserFollowing(userId = currentUser.id, targetUserId = targetUserId) }
            .map { Unit }
    }
}