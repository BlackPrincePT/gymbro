package com.pegio.gymbro.domain.usecase.common

import com.pegio.gymbro.domain.repository.UserRepository
import javax.inject.Inject

class FetchUserByIdUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(id: String) = userRepository.fetchUser(id)
}