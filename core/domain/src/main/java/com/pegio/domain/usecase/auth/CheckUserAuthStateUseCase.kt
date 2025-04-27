package com.pegio.domain.usecase.auth

import com.pegio.auth.repository.AuthRepository
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.common.core.SessionError
import com.pegio.common.core.asFailure
import com.pegio.common.core.asSuccess
import com.pegio.common.core.onFailure
import com.pegio.common.core.onSuccess
import com.pegio.common.core.retryableCall
import com.pegio.datastore.core.CacheManager
import com.pegio.datastore.core.PreferenceKey
import com.pegio.firestore.repository.UserRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CheckUserAuthStateUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val cacheManager: CacheManager
) {
    suspend operator fun invoke(): Resource<Unit, SessionError> {
        val authUser =
            authRepository.getCurrentUser() ?: return SessionError.Unauthenticated.asFailure()

        if (authUser.isAnonymous)
            return SessionError.AnonymousUser.asFailure()

        return isRegistrationComplete(currentUserId = authUser.id)
    }

    private suspend fun isRegistrationComplete(currentUserId: String): Resource<Unit, SessionError> {
        val cachedIsRegistrationComplete = cacheManager
            .observe(key = PreferenceKey.RegistrationState(currentUserId))
            .first()

        if (cachedIsRegistrationComplete == true)
            return Unit.asSuccess()

        retryableCall { userRepository.fetchUserById(id = currentUserId) }
            .onSuccess {
                cacheManager.save(key = PreferenceKey.RegistrationState(currentUserId), true)
            }
            .onFailure { error ->
                return when (error) {
                    DataError.Firestore.DocumentNotFound -> SessionError.RegistrationIncomplete
                    else -> SessionError.Unknown
                }
                    .asFailure()
            }

        return Unit.asSuccess()
    }
}