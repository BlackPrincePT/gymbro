package com.pegio.data.repository

import android.content.Context
import com.pegio.auth.core.AuthHandler
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.domain.repository.AuthRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val authHandler: AuthHandler
) : AuthRepository {

    override fun hasSavedAuthSession() = authHandler.getCurrentUser() != null
    override fun isAnonymousSession() = authHandler.getCurrentUser()?.isAnonymous == true
    override fun getCurrentUserId() = authHandler.getCurrentUser()?.id
    override fun observeCurrentUserId() = authHandler.getCurrentUserStream().map { it?.id }

    // ========= Authentication ========= \\

    override suspend fun signInAnonymously(): Resource<Unit, DataError.Auth> {
        return authHandler.signInAnonymously()
    }

    override suspend fun launchGoogleAuthOptions(context: Context): Resource<Unit, DataError.Auth> {
        return authHandler.launchGoogleAuthOptions(context)
    }

    override fun signOut() = authHandler.signOut()
}