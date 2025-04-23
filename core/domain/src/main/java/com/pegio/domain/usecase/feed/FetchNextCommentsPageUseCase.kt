package com.pegio.domain.usecase.feed

import com.pegio.common.core.asFailure
import com.pegio.common.core.asSuccess
import com.pegio.common.core.getOrElse
import com.pegio.common.core.getOrNull
import com.pegio.common.core.retryableCall
import com.pegio.firestore.repository.PostCommentRepository
import com.pegio.firestore.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class FetchNextCommentsPageUseCase @Inject constructor(
    private val postCommentRepository: PostCommentRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(postId: String) = coroutineScope {
        postCommentRepository.fetchNextCommentsPage(postId)
            .getOrElse { return@coroutineScope it.asFailure() }
            .map { comment ->
                async {
                    retryableCall { userRepository.fetchUserById(id = comment.authorId) }
                        .getOrNull()
                        .let { comment to it }
                }
            }
            .awaitAll()
            .asSuccess()
    }
}