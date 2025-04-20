package com.pegio.domain.usecase.feed

import com.pegio.common.core.Resource
import com.pegio.common.core.SessionError
import com.pegio.common.core.asFailure
import com.pegio.common.core.asSuccess
import com.pegio.domain.repository.AuthRepository
import com.pegio.domain.repository.VoteRepository
import com.pegio.model.Vote
import javax.inject.Inject

class VotePostUseCase @Inject constructor(
    private val voteRepository: VoteRepository,
    private val authRepository: AuthRepository
) {
    operator fun invoke(postId: String, voteType: Vote.Type): Resource<Vote, SessionError> {
        val currentUser =
            authRepository.getCurrentUser() ?: return SessionError.Unauthenticated.asFailure()

        if (currentUser.isAnonymous)
            return SessionError.AnonymousUser.asFailure()

        val vote = Vote(
            voterId = currentUser.id,
            vote = voteType,
            timestamp = System.currentTimeMillis()
        )

        voteRepository.votePost(postId, vote)

        return vote.asSuccess()
    }
}