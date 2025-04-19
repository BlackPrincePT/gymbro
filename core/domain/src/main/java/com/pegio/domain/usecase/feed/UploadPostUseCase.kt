package com.pegio.domain.usecase.feed

import com.pegio.model.Post
import com.pegio.domain.repository.PostRepository
import javax.inject.Inject

class UploadPostUseCase @Inject constructor(private val postRepository: PostRepository) {
    operator fun invoke(post: Post) = postRepository.uploadPost(post)
}