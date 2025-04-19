package com.pegio.domain.usecase.common

import com.pegio.common.core.isSuccess
import com.pegio.domain.repository.AuthRepository
import com.pegio.domain.repository.UserRepository
import com.pegio.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class GetCurrentUserStreamUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<User> {
        return authRepository.getCurrentUserStream()
            .filterNotNull()
            .flatMapLatest { authUser ->
                userRepository.fetchUserSteamById(id = authUser.id)
                    .transform {
                        if (it.isSuccess()) emit(it.data)
                    }
            }
    }
}