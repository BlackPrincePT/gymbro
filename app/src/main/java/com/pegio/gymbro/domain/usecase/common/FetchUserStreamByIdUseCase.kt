package com.pegio.gymbro.domain.usecase.common

import com.pegio.gymbro.domain.repository.UserRepository
import javax.inject.Inject

class FetchUserStreamByIdUseCase @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(id: String) = userRepository.fetchUserSteam(id)
}