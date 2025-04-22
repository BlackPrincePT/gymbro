package com.pegio.domain.usecase.feed

import com.pegio.auth.repository.AuthRepository
import com.pegio.common.core.Resource
import com.pegio.common.core.SessionError
import com.pegio.common.core.asFailure
import com.pegio.common.core.asSuccess
import com.pegio.firestore.repository.PostCommentRepository
import com.pegio.model.PostComment
import javax.inject.Inject

class WriteCommentUseCase @Inject constructor(
    private val postCommentRepository: PostCommentRepository,
    private val authRepository: AuthRepository
) {
    operator fun invoke(content: String, postId: String): Resource<PostComment, SessionError> {
        val authUser =
            authRepository.getCurrentUser() ?: return SessionError.Unauthenticated.asFailure()

        if (authUser.isAnonymous)
            return SessionError.AnonymousUser.asFailure()

        val newComment = PostComment(
            id = "",
            authorId = authUser.id,
            content = content,
            timestamp = System.currentTimeMillis()
        )

        postCommentRepository.writeComment(newComment, postId)

        return newComment.asSuccess()
    }
}