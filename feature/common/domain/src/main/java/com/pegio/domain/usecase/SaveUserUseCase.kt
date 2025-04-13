package com.pegio.domain.usecase

import com.pegio.domain.model.User
import com.pegio.domain.repository.UserRepository
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(user: User) = userRepository.saveUser(user)
}