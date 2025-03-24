package com.pegio.gymbro.domain.repository

import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Resource

interface AuthRepository {
    suspend fun signInWithGoogle(idToken: String): Resource<Unit, DataError.Firebase>
    suspend fun signInAnonymously(): Resource<Unit, DataError.Firebase>
    fun hasSavedAuthSession(): Boolean
    fun isAnonymousSession(): Boolean
    fun signOut()
}