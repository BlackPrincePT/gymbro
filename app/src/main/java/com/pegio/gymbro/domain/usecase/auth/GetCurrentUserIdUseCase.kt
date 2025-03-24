package com.pegio.gymbro.domain.usecase.auth

import com.pegio.gymbro.domain.repository.UserRepository
import javax.inject.Inject

class GetCurrentUserIdUseCase @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke() = userRepository.getCurrentUserId()
}