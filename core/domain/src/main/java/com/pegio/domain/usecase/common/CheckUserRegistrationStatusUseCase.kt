package com.pegio.domain.usecase.common

import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.datastore.core.CacheManager
import com.pegio.datastore.core.PreferenceKey
import com.pegio.domain.repository.AuthRepository
import com.pegio.domain.repository.UserRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class CheckUserRegistrationStatusUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val cacheManager: CacheManager
) {
    suspend operator fun invoke(): Resource<Boolean, DataError.Auth> {
        val currentUser = authRepository.getCurrentUser()
            ?: return Resource.Failure(error = DataError.Auth.UNAUTHENTICATED)

        if (currentUser.isAnonymous)
            return Resource.Success(data = true)

        return when (val isComplete = isRegistrationComplete(currentUser.id)) {
            true, false -> Resource.Success(data = isComplete)
            null -> {
                authRepository.signOut()
                return Resource.Failure(error = DataError.Auth.UNAUTHENTICATED)
            }
        }
    }

    private suspend fun isRegistrationComplete(currentUserId: String): Boolean? {
        val registrationStateKey = PreferenceKey.RegistrationState(userId = currentUserId)

        cacheManager.observe(key = registrationStateKey).firstOrNull()?.let { return it }

        return when (val result = userRepository.fetchUser(id = currentUserId)) {
            is Resource.Success -> {
                cacheManager.save(key = registrationStateKey, true)
                true
            }

            is Resource.Failure -> {
                if (result.error == DataError.Firestore.DOCUMENT_NOT_FOUND) {
                    cacheManager.save(key = registrationStateKey, false)
                    false
                } else null
            }
        }
    }
}