package com.pegio.domain.usecase.auth

import com.pegio.auth.repository.AuthRepository
import com.pegio.common.core.Error
import com.pegio.common.core.Resource
import com.pegio.common.core.SessionError
import com.pegio.common.core.asFailure
import com.pegio.common.core.asSuccess
import com.pegio.common.core.getOrElse
import com.pegio.firestore.repository.UserRepository
import com.pegio.model.User
import com.pegio.uploadmanager.core.FileUploadManager
import javax.inject.Inject

class RegisterCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val uploadManager: FileUploadManager
) {
    suspend operator fun invoke(
        username: String,
        age: Int,
        gender: User.Gender,
        height: Int,
        weight: Int,
        imageUri: String?
    ): Resource<Unit, Error> {

        val currentUser =
            authRepository.getCurrentUser() ?: return SessionError.Unauthenticated.asFailure()

        if (currentUser.isAnonymous)
            return SessionError.AnonymousUser.asFailure()

        var newUser = User(
            id = currentUser.id,
            username = username,
            age = age,
            gender = gender,
            heightCm = height,
            weightKg = weight,
            imgProfileUrl = null,
            imgBackgroundUrl = null,
            followingCount = 0,
            followersCount = 0
        )

        if (imageUri != null) {
            uploadManager.enqueueFileUpload(imageUri)
                .getOrElse { return it.asFailure() }
                .let { newUser = newUser.copy(imgProfileUrl = it) }
        }

        userRepository.saveUser(newUser)

        return Unit.asSuccess()
    }
}