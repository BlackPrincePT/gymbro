package com.pegio.domain.usecase.feed

import com.pegio.common.core.asResource
import com.pegio.common.core.get
import com.pegio.common.core.getOrNull
import com.pegio.common.core.onFailure
import com.pegio.domain.repository.PostRepository
import com.pegio.domain.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class FetchPostByIdUseCase @Inject constructor(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(id: String) = coroutineScope {
        postRepository.fetchPostById(id)
            .onFailure { return@coroutineScope it.asResource() }
            .get()
            .let { post ->
                userRepository.fetchUserById(id = post.authorId)
                    .getOrNull()
                    .let { post to it }
            }
            .asResource()
    }
}