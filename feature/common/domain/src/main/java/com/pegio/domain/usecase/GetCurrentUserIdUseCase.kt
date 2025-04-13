package com.pegio.domain.usecase

import com.pegio.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserIdUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke() = authRepository.getCurrentUserId()
        .also { if (it == null) authRepository.signOut() } ?: throw Exception()
}