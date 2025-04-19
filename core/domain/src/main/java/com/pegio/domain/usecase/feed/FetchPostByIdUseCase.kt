package com.pegio.domain.usecase.feed

import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.common.core.asFailure
import com.pegio.common.core.asSuccess
import com.pegio.common.core.getOrElse
import com.pegio.common.core.getOrNull
import com.pegio.common.core.retryableCall
import com.pegio.domain.repository.PostRepository
import com.pegio.domain.repository.UserRepository
import com.pegio.model.Post
import com.pegio.model.User
import javax.inject.Inject

class FetchPostByIdUseCase @Inject constructor(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(id: String): Resource<Pair<Post, User?>, DataError.Firestore> {
        postRepository.fetchPostById(id)
            .getOrElse { return it.asFailure() }
            .let { post ->
                return retryableCall { userRepository.fetchUserById(id = post.authorId) }
                    .getOrNull()
                    .let { post to it }
                    .asSuccess()
            }
    }
}