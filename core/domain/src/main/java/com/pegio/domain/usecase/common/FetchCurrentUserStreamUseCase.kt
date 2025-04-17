package com.pegio.domain.usecase.common

import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.domain.repository.AuthRepository
import com.pegio.domain.repository.UserRepository
import com.pegio.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class FetchCurrentUserStreamUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Resource<User, DataError.Firestore>> {
        return authRepository.getCurrentUserStream()
            .filterNotNull()
            .flatMapLatest { currentUser ->
                userRepository.fetchUserSteam(id = currentUser.id)
            }
    }
}