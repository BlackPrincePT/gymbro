package com.pegio.domain.usecase.common

import com.pegio.auth.repository.AuthRepository
import com.pegio.common.core.DataError
import com.pegio.common.core.Error
import com.pegio.common.core.Resource
import com.pegio.common.core.SessionError
import com.pegio.common.core.asFailure
import com.pegio.common.core.asSuccess
import com.pegio.common.core.isSuccess
import com.pegio.common.core.onFailure
import com.pegio.common.core.onSuccess
import com.pegio.firestore.repository.UserRepository
import com.pegio.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class GetCurrentUserStreamUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Resource<User, SessionError.AnonymousUser>> {
        return authRepository.getCurrentUserStream()
            .filterNotNull()
            .flatMapLatest { authUser ->
                userRepository.fetchUserSteamById(id = authUser.id)
                    .transform { resource ->
                        resource
                            .onSuccess { emit(it.asSuccess()) }
                            .onFailure {
                                if (authUser.isAnonymous)
                                    emit(SessionError.AnonymousUser.asFailure())
                            }
                    }
            }
    }
}