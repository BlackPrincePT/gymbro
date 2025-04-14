package com.pegio.domain.usecase

import com.pegio.domain.repository.PostRepository
import javax.inject.Inject

class ObserveRelevantPostsStreamUseCase @Inject constructor(private val postRepository: PostRepository) {
    suspend operator fun invoke() = postRepository.loadRelevantPosts()
}