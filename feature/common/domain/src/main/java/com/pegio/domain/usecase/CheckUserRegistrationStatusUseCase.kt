package com.pegio.domain.usecase

import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.datastore.core.CacheManager
import com.pegio.datastore.core.PreferenceKey
import com.pegio.domain.repository.AuthRepository
import com.pegio.domain.repository.UserRepository
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
        if (cacheManager.observe<Boolean>(key = PreferenceKey.AuthState).first() == true)
            return true

        return when (val resource = userRepository.fetchUser(id = currentUserId)) {
            is Resource.Success -> {
                cacheManager.save(key = PreferenceKey.AuthState, true)
                true
            }

            is Resource.Failure -> {
                if (resource.error == DataError.Firestore.DOCUMENT_NOT_FOUND) false
                else null
            }
        }
    }
}