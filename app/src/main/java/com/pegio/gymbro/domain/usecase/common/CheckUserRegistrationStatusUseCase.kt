package com.pegio.gymbro.domain.usecase.common

import com.pegio.gymbro.domain.manager.cache.CacheManager
import com.pegio.gymbro.domain.manager.cache.PreferenceKeys
import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.repository.AuthRepository
import com.pegio.gymbro.domain.repository.UserRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CheckUserRegistrationStatusUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val cacheManager: CacheManager
) {
    suspend operator fun invoke(): Resource<Boolean, DataError.Auth> {
        val currentUserId = authRepository.getCurrentUserId()
            ?: run { authRepository.signOut(); return Resource.Failure(error = DataError.Auth.UNAUTHENTICATED) }

        val registrationComplete = isRegistrationComplete(currentUserId)
            ?: run { authRepository.signOut(); return Resource.Failure(error = DataError.Auth.UNAUTHENTICATED) }

        val isAnonymous = authRepository.isAnonymousSession()
        return Resource.Success(data = isAnonymous || registrationComplete)
    }


    private suspend fun isRegistrationComplete(currentUserId: String): Boolean? {
        if (cacheManager.observe(key = PreferenceKeys.AUTH_STATE_KEY).first() == true)
            return true

        return when (val resource = userRepository.fetchUser(id = currentUserId)) {
            is Resource.Success -> {
                cacheManager.save(key = PreferenceKeys.AUTH_STATE_KEY, true)
                true
            }

            is Resource.Failure -> {
                if (resource.error == DataError.Firestore.DOCUMENT_NOT_FOUND) false
                else null
            }
        }
    }
}