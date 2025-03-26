package com.pegio.gymbro.domain.repository

import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Resource

interface AuthRepository {
    suspend fun signInWithGoogle(idToken: String): Resource<Unit, DataError.FirebaseAuth>
    suspend fun signInAnonymously(): Resource<Unit, DataError.FirebaseAuth>
    fun hasSavedAuthSession(): Boolean
    fun isAnonymousSession(): Boolean
    fun signOut()
}