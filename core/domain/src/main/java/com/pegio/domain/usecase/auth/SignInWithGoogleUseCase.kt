package com.pegio.domain.usecase.auth

import android.content.Context
import com.pegio.auth.repository.AuthRepository
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.common.core.asFailure
import com.pegio.common.core.getOrElse
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(context: Context): Resource<Unit, DataError.Auth> {
        return authRepository.launchGoogleAuthOptionsAndCreateToken(context)
            .getOrElse { return it.asFailure() }
            .let { authRepository.signInWithGoogle(it) }
    }
}