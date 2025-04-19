package com.pegio.domain.usecase.feed

import com.pegio.common.core.asResource
import com.pegio.common.core.get
import com.pegio.common.core.getOrNull
import com.pegio.common.core.onFailure
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
            .onFailure { return@coroutineScope it.asResource() }
            .get()
            .map { post ->
                async {
                    userRepository.fetchUserById(id = post.authorId)
                        .getOrNull() // TODO: IMPLEMENT RETRY POLICY
                        .let { post to it }
                }
            }
            .awaitAll()
            .asResource()
    }
}