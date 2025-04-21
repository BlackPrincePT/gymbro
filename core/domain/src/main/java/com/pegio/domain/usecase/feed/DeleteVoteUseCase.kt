package com.pegio.domain.usecase.feed

import com.pegio.common.core.Resource
import com.pegio.common.core.SessionError
import com.pegio.common.core.asFailure
import com.pegio.common.core.asSuccess
import com.pegio.domain.repository.AuthRepository
import com.pegio.domain.repository.VoteRepository
import javax.inject.Inject

class DeleteVoteUseCase @Inject constructor(
    private val voteRepository: VoteRepository,
    private val authRepository: AuthRepository
) {
    operator fun invoke(postId: String): Resource<Unit, SessionError> {
        val currentUser =
            authRepository.getCurrentUser() ?: return SessionError.Unauthenticated.asFailure()

        if (currentUser.isAnonymous)
            return SessionError.AnonymousUser.asFailure()

        voteRepository.deleteVote(postId = postId, voterId = currentUser.id)

        return Unit.asSuccess()
    }
}