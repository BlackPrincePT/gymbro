package com.pegio.auth.core

import android.content.Context
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.model.User
import kotlinx.coroutines.flow.Flow

interface AuthHandler {
    fun getCurrentUser(): User.Auth?
    fun getCurrentUserStream(): Flow<User.Auth?>
    suspend fun launchGoogleAuthOptions(context: Context): Resource<Unit, DataError.Auth>
    suspend fun signInAnonymously(): Resource<Unit, DataError.Auth>
    fun signOut()
}