package com.pegio.domain.usecase.feed

import com.pegio.auth.repository.AuthRepository
import com.pegio.common.core.Error
import com.pegio.common.core.Resource
import com.pegio.common.core.SessionError
import com.pegio.common.core.asFailure
import com.pegio.common.core.asSuccess
import com.pegio.common.core.getOrElse
import com.pegio.common.core.retryableCall
import com.pegio.firestore.repository.PostRepository
import com.pegio.model.Post
import com.pegio.uploadmanager.core.UploadManager
import javax.inject.Inject

class UploadPostUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val postRepository: PostRepository,
    private val uploadManager: UploadManager
) {
    suspend operator fun invoke(
        content: String,
        imageUri: String? = null,
        workoutId: String? = null
    ): Resource<Unit, Error> {

        val currentUser =
            authRepository.getCurrentUser() ?: return SessionError.Unauthenticated.asFailure()

        if (currentUser.isAnonymous)
            return SessionError.AnonymousUser.asFailure()

        val newPost = Post.EMPTY
            .copy(
                authorId = currentUser.id,
                workoutId = workoutId,
                content = content,
                timestamp = System.currentTimeMillis()
            )

        if (imageUri == null) {
            postRepository.uploadPost(newPost)
            return Unit.asSuccess()
        }

        retryableCall { uploadManager.enqueueFileUpload(imageUri) }
            .getOrElse { return it.asFailure() }
            .let {
                postRepository.uploadPost(post = newPost.copy(imageUrl = it))
                return Unit.asSuccess()
            }
    }
}