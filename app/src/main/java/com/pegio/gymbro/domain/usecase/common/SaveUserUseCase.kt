package com.pegio.gymbro.domain.usecase.common

import com.pegio.gymbro.domain.model.User
import com.pegio.gymbro.domain.repository.UserRepository
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(user: User) = userRepository.saveUser(user)
}