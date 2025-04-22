package com.pegio.domain.usecase.splash

import com.pegio.auth.repository.AuthRepository
import javax.inject.Inject

class HasSavedAuthSessionUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke() = authRepository.getCurrentUser() != null
}