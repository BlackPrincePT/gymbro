package com.pegio.gymbro.domain.usecase.createpost

import com.pegio.gymbro.domain.model.Post
import com.pegio.gymbro.domain.repository.PostRepository
import javax.inject.Inject

class UploadPostUseCase @Inject constructor(private val postRepository: PostRepository) {
    operator fun invoke(post: Post) = postRepository.uploadPost(post)
}