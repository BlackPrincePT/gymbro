package com.pegio.gymbro.domain.usecase.splash

import com.pegio.gymbro.domain.repository.AuthRepository
import javax.inject.Inject

class HasSavedAuthSessionUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke() = authRepository.hasSavedAuthSession()
}