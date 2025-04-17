package com.pegio.domain.usecase.account

import com.pegio.domain.repository.AuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke() = authRepository.signOut()
}