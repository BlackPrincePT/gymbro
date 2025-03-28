package com.pegio.gymbro.domain.usecase.common

import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.repository.AuthRepository
import com.pegio.gymbro.domain.repository.UserRepository
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchCurrentUserStreamUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke() = flow {
        authRepository.getCurrentUserId()?.let { currentUserId ->
            emitAll(userRepository.fetchUserSteam(id = currentUserId))
        } ?: emit(Resource.Failure(error = DataError.Firestore.UNAUTHENTICATED))
    }
}