package com.pegio.gymbro.domain.usecase.auth

import com.pegio.gymbro.domain.repository.AuthRepository
import javax.inject.Inject

class SignInAnonymouslyUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke() = authRepository.signInAnonymously()
}