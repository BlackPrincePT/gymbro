package com.pegio.domain.usecase.auth

import com.pegio.auth.repository.AuthRepository
import javax.inject.Inject

class SignInAnonymouslyUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke() = authRepository.signInAnonymously()
}