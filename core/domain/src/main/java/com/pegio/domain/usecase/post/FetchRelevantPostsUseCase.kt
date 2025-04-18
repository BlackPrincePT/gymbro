package com.pegio.domain.usecase.post

import com.pegio.domain.repository.PostRepository
import javax.inject.Inject

class FetchRelevantPostsUseCase @Inject constructor(private val postRepository: PostRepository) {
    suspend operator fun invoke() = postRepository.loadRelevantPosts()
}