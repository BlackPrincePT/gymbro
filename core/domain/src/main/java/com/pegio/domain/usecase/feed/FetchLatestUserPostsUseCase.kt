package com.pegio.domain.usecase.feed

import com.pegio.auth.repository.AuthRepository
import com.pegio.common.core.asFailure
import com.pegio.common.core.asSuccess
import com.pegio.common.core.getOrElse
import com.pegio.common.core.getOrNull
import com.pegio.common.core.retryableCall
import com.pegio.domain.model.PostWithAuthorAndVote
import com.pegio.firestore.repository.PostRepository
import com.pegio.firestore.repository.UserRepository
import com.pegio.firestore.repository.VoteRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class FetchLatestUserPostsUseCase @Inject constructor(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val voteRepository: VoteRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(authorId: String) = coroutineScope {
        val authorDeferred = async {
            retryableCall { userRepository.fetchUserById(id = authorId) }
                .getOrNull()
        }

        val currentUser = authRepository.getCurrentUser()

        retryableCall { postRepository.fetchLatestUserPostsPage(authorId) }
            .getOrElse { return@coroutineScope it.asFailure() }
            .map { post ->
                async {
                    val voteDeferred = async {
                        currentUser?.let {
                            retryableCall { voteRepository.checkForPostVote(currentUser.id, post.id) }
                                .getOrNull()
                        }
                    }

                    PostWithAuthorAndVote(
                        post = post,
                        author = authorDeferred.await(),
                        currentUserVote = voteDeferred.await()
                    )
                }
            }
            .awaitAll()
            .asSuccess()
    }
}