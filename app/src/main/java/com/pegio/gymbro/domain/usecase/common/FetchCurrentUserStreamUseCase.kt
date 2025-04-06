package com.pegio.gymbro.domain.usecase.common

import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.model.User
import com.pegio.gymbro.domain.repository.AuthRepository
import com.pegio.gymbro.domain.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
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