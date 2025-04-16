package com.pegio.domain.repository

import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signInWithGoogle(idToken: String): Resource<Unit, DataError.Auth>
    suspend fun signInAnonymously(): Resource<Unit, DataError.Auth>
    fun hasSavedAuthSession(): Boolean
    fun isAnonymousSession(): Boolean
    fun getCurrentUserId(): String?
    fun observeCurrentUserId(): Flow<String?>
    fun signOut()
}