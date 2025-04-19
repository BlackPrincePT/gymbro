package com.pegio.domain.usecase.common

import com.pegio.common.core.Resource
import com.pegio.common.core.SessionError
import com.pegio.common.core.asFailure
import com.pegio.common.core.asSuccess
import com.pegio.common.core.isSuccess
import com.pegio.domain.repository.AuthRepository
import com.pegio.domain.repository.UserRepository
import com.pegio.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class GetCurrentUserStreamUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(): Resource<Flow<User>, SessionError> {
        val authUser =
            authRepository.getCurrentUser() ?: return SessionError.Unauthenticated.asFailure()

        if (authUser.isAnonymous)
            return SessionError.AnonymousUser.asFailure()

        return userRepository.fetchUserSteamById(id = authUser.id)
            .transform { if (it.isSuccess()) emit(it.data) }
            .asSuccess()
    }
}