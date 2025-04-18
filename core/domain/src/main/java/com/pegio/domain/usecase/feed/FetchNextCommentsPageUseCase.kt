package com.pegio.domain.usecase.feed

import com.pegio.domain.repository.PostCommentRepository
import javax.inject.Inject

class FetchNextCommentsPageUseCase @Inject constructor(private val postCommentRepository: PostCommentRepository) {
    suspend operator fun invoke(postId: String) = postCommentRepository.fetchNextCommentsPage(postId)
}