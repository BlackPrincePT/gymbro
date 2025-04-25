package com.pegio.domain.usecase.feed

import com.pegio.firestore.repository.PostCommentRepository
import javax.inject.Inject

class ResetPostCommentPaginationUseCase @Inject constructor(private val postCommentRepository: PostCommentRepository) {
    operator fun invoke() = postCommentRepository.resetPagination()
}