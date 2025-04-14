package com.pegio.domain.usecase

import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.domain.model.User
import com.pegio.domain.repository.AuthRepository
import com.pegio.domain.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class FetchCurrentUserStreamUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Resource<User, DataError.Firestore>> {
        return authRepository.observeCurrentUserId()
            .filterNotNull()
            .flatMapLatest { currentUserId ->
                userRepository.fetchUserSteam(id = currentUserId)
            }
    }
}