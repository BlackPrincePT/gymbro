package com.pegio.domain.usecase.feed

import com.pegio.common.core.asResource
import com.pegio.common.core.getOrElse
import com.pegio.common.core.getOrNull
import com.pegio.common.core.retryableCall
import com.pegio.domain.repository.PostRepository
import com.pegio.domain.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class FetchNextRelevantPostsPageUseCase @Inject constructor(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() = coroutineScope {
        postRepository.fetchNextRelevantPostsPage()
            .getOrElse { return@coroutineScope it.asResource() }
            .map { post ->
                async {
                    retryableCall { userRepository.fetchUserById(id = post.authorId) }
                        .getOrNull()
                        .let { post to it }
                }
            }
            .awaitAll()
            .asResource()
    }
}