package com.pegio.domain.usecase.feed

import com.pegio.domain.repository.PostCommentRepository
import com.pegio.model.PostComment
import javax.inject.Inject

class WriteCommentUseCase @Inject constructor(private val postCommentRepository: PostCommentRepository) {
    operator fun invoke(comment: PostComment, postId: String) = postCommentRepository.writeComment(comment, postId)
}