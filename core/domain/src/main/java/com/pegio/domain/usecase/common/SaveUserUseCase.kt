package com.pegio.domain.usecase.common

import com.pegio.firestore.repository.UserRepository
import com.pegio.model.User
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(user: User) = userRepository.saveUser(user)
}