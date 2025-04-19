package com.pegio.session.core

import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.common.core.SessionError
import com.pegio.common.core.asResource
import com.pegio.common.core.errorOrElse
import com.pegio.common.core.onFailure
import com.pegio.common.core.onSuccess
import com.pegio.common.core.retryableCall
import com.pegio.domain.repository.AuthRepository
import com.pegio.domain.repository.UserRepository
import com.pegio.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.transform
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {

    fun getCurrentUserStream(): Flow<User> {
        return authRepository.getCurrentUserStream()
            .filterNotNull()
            .flatMapLatest { authUser ->
                userRepository.fetchUserSteamById(id = authUser.id)
                    .onFailure { } // TODO: RETRY POLICY
                    .transform { resource ->
                        resource.onSuccess { emit(it) }
                    }
            }
    }

    suspend fun getCurrentUser(): Resource<User, SessionError> {
        val authUser =
            authRepository.getCurrentUser() ?: return SessionError.Unauthenticated.asResource()

        retryableCall { userRepository.fetchUserById(id = authUser.id) }
            .errorOrElse { return it.asResource() }
            .let { error ->
                return when (error) {
                    DataError.Firestore.DOCUMENT_NOT_FOUND, DataError.Firestore.DOCUMENT_PARSE_FAILED -> SessionError.RegistrationIncomplete
                    else -> SessionError.Unknown
                }
                    .asResource()
            }
    }
}