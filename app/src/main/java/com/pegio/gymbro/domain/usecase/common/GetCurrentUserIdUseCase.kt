package com.pegio.gymbro.domain.usecase.common

import com.pegio.gymbro.domain.core.UserAuthenticationException
import com.pegio.gymbro.domain.repository.AuthRepository
import com.pegio.gymbro.domain.repository.UserRepository
import javax.inject.Inject

class GetCurrentUserIdUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) {
    operator fun invoke(): String {
        val currentUserId = userRepository.getCurrentUserId()

        if (currentUserId == null) {
            authRepository.signOut()
            throw UserAuthenticationException()
        }

        return currentUserId
    }
}