package com.pegio.gymbro.domain.usecase.common

import com.pegio.gymbro.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserIdUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke() = authRepository.getCurrentUserId()
        .also { if (it == null) authRepository.signOut() } ?: throw Exception()
}