package com.pegio.domain.usecase.feed

import com.pegio.common.core.asResource
import com.pegio.common.core.get
import com.pegio.common.core.getOrNull
import com.pegio.common.core.onFailure
import com.pegio.domain.repository.PostCommentRepository
import com.pegio.domain.repository.UserRepository
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
            .onFailure { return@coroutineScope it.asResource() }
            .get()
            .map { comment ->
                async {
                    userRepository.fetchUserById(id = comment.authorId)
                        .getOrNull() // TODO: IMPLEMENT RETRY POLICY
                        .let { comment to it }
                }
            }
            .awaitAll()
            .asResource()
    }
}