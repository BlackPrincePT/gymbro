package com.pegio.gymbro.domain.usecase.auth

import com.pegio.gymbro.domain.repository.AuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke() = authRepository.signOut()
}