package com.pegio.auth.repository

import android.content.Context
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getCurrentUser(): User.Auth?
    fun getCurrentUserStream(): Flow<User.Auth?>

    suspend fun signInWithGoogle(token: String): Resource<Unit, DataError.Auth>
    suspend fun signInAnonymously(): Resource<Unit, DataError.Auth>
    suspend fun linkAnonymousAccount(token: String): Resource<Unit, DataError.Auth>

    suspend fun launchGoogleAuthOptionsAndCreateToken(context: Context): Resource<String, DataError.Auth>

    fun signOut()
}