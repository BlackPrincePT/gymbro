package com.pegio.gymbro.domain.usecase.home

import com.pegio.gymbro.domain.repository.PostRepository
import javax.inject.Inject

class ObserveRelevantPostsStreamUseCase @Inject constructor(private val postRepository: PostRepository) {
    operator fun invoke() = postRepository.observePostsPagingStream()
}