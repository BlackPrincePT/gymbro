package com.pegio.domain.usecase.common

import com.pegio.domain.repository.UserRepository
import javax.inject.Inject

class FetchUserByIdUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(id: String) = userRepository.fetchUserById(id)
}