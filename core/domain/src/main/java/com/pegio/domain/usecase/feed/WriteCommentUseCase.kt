package com.pegio.domain.usecase.feed

import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.common.core.asResource
import com.pegio.domain.repository.AuthRepository
import com.pegio.domain.repository.PostCommentRepository
import com.pegio.model.PostComment
import javax.inject.Inject

class WriteCommentUseCase @Inject constructor(
    private val postCommentRepository: PostCommentRepository,
    private val authRepository: AuthRepository
) {
    operator fun invoke(content: String, postId: String): Resource<PostComment, DataError.Auth> {
        return authRepository.getCurrentUser()?.let { currentUser ->
            val newComment = PostComment(
                id = "",
                authorId = currentUser.id,
                content = content,
                timestamp = System.currentTimeMillis()
            )

            postCommentRepository.writeComment(newComment, postId)

            newComment.asResource()
        } ?: DataError.Auth.UNAUTHENTICATED.asResource()
    }
}