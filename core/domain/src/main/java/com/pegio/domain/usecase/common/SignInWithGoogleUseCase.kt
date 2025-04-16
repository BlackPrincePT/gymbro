package com.pegio.domain.usecase.common

import com.pegio.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(idToken: String) = authRepository.signInWithGoogle(idToken)
}