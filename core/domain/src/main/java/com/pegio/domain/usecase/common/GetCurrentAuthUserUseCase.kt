package com.pegio.domain.usecase.common

import com.pegio.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentAuthUserUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke() = authRepository.getCurrentUser()
}