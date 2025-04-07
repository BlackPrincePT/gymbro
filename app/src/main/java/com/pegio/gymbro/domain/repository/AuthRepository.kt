package com.pegio.gymbro.domain.repository

import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Resource
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