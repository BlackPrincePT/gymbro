package com.pegio.domain.repository

import android.content.Context
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getCurrentUser(): User.Auth?
    suspend fun launchGoogleAuthOptions(context: Context): Resource<Unit, DataError.Auth>
    suspend fun signInAnonymously(): Resource<Unit, DataError.Auth>
    fun signOut()
}