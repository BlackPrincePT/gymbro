package com.pegio.domain.usecase.auth

import com.pegio.auth.repository.AuthRepository
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.common.core.SessionError
import com.pegio.common.core.asFailure
import com.pegio.common.core.asSuccess
import com.pegio.common.core.errorOrElse
import com.pegio.common.core.retryableCall
import com.pegio.firestore.repository.UserRepository
import javax.inject.Inject

class CheckUserAuthStateUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Resource<Unit, SessionError> {
        val authUser =
            authRepository.getCurrentUser() ?: return SessionError.Unauthenticated.asFailure()

        if (authUser.isAnonymous)
            return SessionError.AnonymousUser.asFailure()

        retryableCall { userRepository.fetchUserById(id = authUser.id) }
            .errorOrElse { return Unit.asSuccess() }
            .let { error ->
                return when (error) {
                    DataError.Firestore.DocumentNotFound -> SessionError.RegistrationIncomplete
                    else -> SessionError.Unknown
                }
                    .asFailure()
            }
    }
}