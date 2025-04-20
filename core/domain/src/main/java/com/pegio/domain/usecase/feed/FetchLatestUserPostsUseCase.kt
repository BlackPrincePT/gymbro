package com.pegio.domain.usecase.feed

import com.pegio.domain.repository.PostRepository
import javax.inject.Inject

class FetchLatestUserPostsUseCase @Inject constructor(private val postRepository: PostRepository) {
    suspend operator fun invoke(authorId: String) =
        postRepository.fetchLatestUserPostsPage(authorId)
}