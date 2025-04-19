package com.pegio.domain.usecase.auth

import android.content.Context
import com.pegio.domain.repository.AuthRepository
import javax.inject.Inject

class LaunchGoogleAuthOptionsUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(context: Context) = authRepository.launchGoogleAuthOptions(context)
}